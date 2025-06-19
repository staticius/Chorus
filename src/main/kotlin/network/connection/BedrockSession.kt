package org.chorus_oss.chorus.network.connection

import com.github.oxo42.stateless4j.StateMachine
import com.github.oxo42.stateless4j.StateMachineConfig
import com.github.oxo42.stateless4j.delegates.Action
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.Unpooled
import io.netty.util.internal.PlatformDependent
import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.command.data.CommandDataVersions
import org.chorus_oss.chorus.event.player.PlayerCreationEvent
import org.chorus_oss.chorus.event.server.DataPacketDecodeEvent
import org.chorus_oss.chorus.event.server.DataPacketReceiveEvent
import org.chorus_oss.chorus.event.server.DataPacketSendEvent
import org.chorus_oss.chorus.network.connection.netty.BedrockBatchWrapper
import org.chorus_oss.chorus.network.connection.netty.BedrockPacketWrapper
import org.chorus_oss.chorus.network.connection.netty.codec.packet.BedrockPacketCodec
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.process.DataPacketManager
import org.chorus_oss.chorus.network.process.SessionState
import org.chorus_oss.chorus.network.process.handler.*
import org.chorus_oss.chorus.network.protocol.*
import org.chorus_oss.chorus.network.protocol.types.PacketCompressionAlgorithm
import org.chorus_oss.chorus.network.protocol.types.PlayerInfo
import org.chorus_oss.chorus.plugin.InternalPlugin
import org.chorus_oss.chorus.registry.Registries
import org.chorus_oss.chorus.utils.ByteBufVarInt
import org.chorus_oss.chorus.utils.Loggable
import org.jetbrains.annotations.ApiStatus
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Consumer
import javax.crypto.SecretKey

class BedrockSession(val peer: BedrockPeer, val subClientId: Int) : Loggable {
    private val closed = AtomicBoolean()
    private val inbound: Queue<DataPacket> = PlatformDependent.newSpscQueue()
    private val nettyThreadOwned = AtomicBoolean(false)
    private val consumer = AtomicReference<Consumer<DataPacket>?>(null)

    @JvmField
    val machine: StateMachine<SessionState, SessionState>
    var player: Player? = null
        private set
    private var info: PlayerInfo? = null
    protected var packetHandler: PacketHandler? = null

    @JvmField
    var address: InetSocketAddress?

    var authenticated: Boolean = false
        private set


    init {
        // from session start to log in sequence complete, netty threads own the session
        this.setNettyThreadOwned(true)

        this.setPacketConsumer { pk: DataPacket ->
            try {
                this.handleDataPacket(pk)
            } catch (e: Exception) {
                log.error(
                    "An error occurred whilst handling {} for {}", pk.javaClass.simpleName,
                    socketAddress.toString(), e
                )
            }
        }

        this.address = socketAddress as InetSocketAddress?
        log.debug("creating session {}", peer.socketAddress.toString())
        val cfg = StateMachineConfig<SessionState, SessionState>()

        cfg.configure(SessionState.START)
            .onExit(Action { this.onSessionStartSuccess() })
            .permit(SessionState.LOGIN, SessionState.LOGIN)

        cfg.configure(SessionState.LOGIN).onEntry(Action {
            this.packetHandler = (
                    LoginHandler(
                        this
                    ) { info: PlayerInfo? ->
                        this.info = info
                    })
        })
            .onExit(Action { this.onServerLoginSuccess() })
            .permitIf(
                SessionState.ENCRYPTION, SessionState.ENCRYPTION
            ) { Server.instance.enabledNetworkEncryption }
            .permit(SessionState.RESOURCE_PACK, SessionState.RESOURCE_PACK)

        cfg.configure(SessionState.ENCRYPTION)
            .onEntry(Action {
                log.debug("Player {} enter ENCRYPTION stage", peer.socketAddress.toString())
                this.packetHandler = (HandshakePacketHandler(this))
            })
            .permit(SessionState.RESOURCE_PACK, SessionState.RESOURCE_PACK)

        cfg.configure(SessionState.RESOURCE_PACK)
            .onEntry(Action {
                log.debug("Player {} enter RESOURCE_PACK stage", peer.socketAddress.toString())
                this.packetHandler = (ResourcePackHandler(this))
            })
            .permit(SessionState.PRE_SPAWN, SessionState.PRE_SPAWN)

        cfg.configure(SessionState.PRE_SPAWN)
            .onEntry(Action {
                // now the main thread owns the session
                this.setNettyThreadOwned(false)

                log.debug("Creating player")

                val player = this.createPlayer()
                if (player == null) {
                    this.close("Failed to create player")
                    return@Action
                }
                this.onPlayerCreated(player)
                player.processLogin()
                this.packetHandler = (SpawnResponseHandler(this))
                // The reason why teleport player to their position is for gracefully client-side spawn,
                // although we need some hacks, It is definitely a fairly worthy trade.
                player!!.setImmobile(true) // TODO: HACK: fix client-side falling pre-spawn
            })
            .onExit(Action { this.onClientSpawned() })
            .permit(SessionState.IN_GAME, SessionState.IN_GAME)

        cfg.configure(SessionState.IN_GAME)
            .onEntry(Action { this.packetHandler = (InGamePacketHandler(this)) })
            .onExit(Action { this.onServerDeath() })
            .permit(SessionState.DEATH, SessionState.DEATH)

        cfg.configure(SessionState.DEATH) //.onEntry(()->this.setPacketHandler(new DeathHandler()))
            .onExit(Action { this.onClientRespawn() })
            .permit(SessionState.IN_GAME, SessionState.IN_GAME)

        machine = StateMachine(SessionState.START, cfg)
        this.packetHandler = (SessionStartHandler(this))
    }

    fun setNettyThreadOwned(immediatelyHandle: Boolean) {
        nettyThreadOwned.set(immediatelyHandle)
    }

    fun isNettyThreadOwned(): Boolean {
        return nettyThreadOwned.get()
    }

    fun setPacketConsumer(consumer: Consumer<DataPacket>?) {
        this.consumer.set(consumer)
    }

    fun flush() {
        if (isDisconnected) {
            return
        }
        peer.flush()
    }

    fun sendPacket(packet: DataPacket) {
        if (isDisconnected) {
            return
        }
        val ev = DataPacketSendEvent(player, packet)
        Server.instance.pluginManager.callEvent(ev)
        if (ev.cancelled) {
            return
        }
        peer.sendPacket(this.subClientId, 0, packet)
        this.logOutbound(packet)
    }

    fun sendPlayStatus(status: Int, immediate: Boolean) {
        val pk = PlayStatusPacket()
        pk.status = status
        if (immediate) {
            this.sendPacketImmediately(pk)
        } else {
            this.sendPacket(pk)
        }
    }

    fun sendRawPacket(pid: Int, buf2: ByteBuf) {
        if (isDisconnected) {
            return
        }
        val bedrockPacketCodec = peer.channel.pipeline().get(
            BedrockPacketCodec::class.java
        )
        val buf1 = ByteBufAllocator.DEFAULT.ioBuffer(4)
        val msg = BedrockPacketWrapper(pid, this.subClientId, 0, null, null)
        bedrockPacketCodec.encodeHeader(buf1, msg)
        val compositeBuf = Unpooled.compositeBuffer()
        compositeBuf.addComponents(true, buf1, buf2)
        msg.packetBuffer = compositeBuf
        peer.sendRawPacket(msg)
    }

    fun sendPacketImmediately(packet: DataPacket) {
        if (isDisconnected) {
            return
        }

        val ev = DataPacketSendEvent(player, packet)
        Server.instance.pluginManager.callEvent(ev)
        if (ev.cancelled) {
            return
        }

        peer.sendPacketImmediately(this.subClientId, 0, packet)
        this.logOutbound(packet)
    }

    fun sendPacketSync(packet: DataPacket) {
        if (isDisconnected) {
            return
        }
        val ev = DataPacketSendEvent(player, packet)
        Server.instance.pluginManager.callEvent(ev)
        if (ev.cancelled) {
            return
        }
        peer.sendPacketSync(this.subClientId, 0, packet)
        this.logOutbound(packet)
    }

    fun sendNetworkSettingsPacket(pk: NetworkSettingsPacket) {
        val alloc = peer.channel.alloc()
        val buf1 = alloc.buffer(16)
        val header = alloc.ioBuffer(5)
        val msg = BedrockPacketWrapper(0, subClientId, 0, pk, null)
        try {
            val bedrockPacketCodec = peer.channel.pipeline().get(BedrockPacketCodec.NAME) as BedrockPacketCodec
            val packet = msg.packet
            msg.packetId = packet!!.pid()
            bedrockPacketCodec.encodeHeader(buf1, msg)
            packet.encode(HandleByteBuf.of(buf1))

            val batch: BedrockBatchWrapper = BedrockBatchWrapper.newInstance()
            val buf2 = alloc.compositeDirectBuffer(2)
            ByteBufVarInt.writeUnsignedInt(header, buf1.readableBytes())
            buf2.addComponent(true, header)
            buf2.addComponent(true, buf1)
            batch.setCompressed(buf2)
            peer.channel.writeAndFlush(batch)
        } catch (t: Throwable) {
            log.error("Error send", t)
        } finally {
            msg.release()
        }
    }

    fun flushSendBuffer() {
        if (isDisconnected) {
            return
        }
        peer.flushSendQueue()
    }

    fun setCompression(algorithm: PacketCompressionAlgorithm) {
        check(!isSubClient) { "The compression algorithm can only be set by the primary session" }
        peer.setCompression(algorithm)
    }

    fun enableEncryption(key: SecretKey) {
        check(!isSubClient) { "Encryption can only be enabled by the primary session" }
        peer.enableEncryption(key)
    }

    fun onPacket(wrapper: BedrockPacketWrapper) {
        val packet = wrapper.packet!!
        this.logInbound(packet)

        val ev = DataPacketDecodeEvent(player, wrapper)
        Server.instance.pluginManager.callEvent(ev)

        val predictMaxBuffer = when (ev.packetId) {
            ProtocolInfo.LOGIN_PACKET -> 10000000
            ProtocolInfo.PLAYER_SKIN_PACKET -> 5000000
            else -> 25000
        }
        if (ev.packetBuffer.length > predictMaxBuffer) {
            ev.cancelled = true
        }

        if (ev.cancelled) return

        if (nettyThreadOwned.get()) {
            val c = consumer.get()
            c?.accept(packet)
        } else {
            inbound.add(packet)
        }
    }

    protected fun logOutbound(packet: DataPacket) {
        if (Server.instance.isLoggedPacket(packet.javaClass)) {
            log.info(
                "Outbound {}({}): {}",
                socketAddress, this.subClientId, packet
            )
        } else if (log.isTraceEnabled && !Server.instance.isIgnoredPacket(packet.javaClass)) {
            log.trace(
                "Outbound {}({}): {}",
                socketAddress, this.subClientId, packet
            )
        }
    }

    protected fun logInbound(packet: DataPacket) {
        if (Server.instance.isLoggedPacket(packet.javaClass)) {
            log.info(
                "Inbound {}({}): {}",
                socketAddress, this.subClientId, packet
            )
        } else if (log.isTraceEnabled && !Server.instance.isIgnoredPacket(packet.javaClass)) {
            log.trace(
                "Inbound {}({}): {}",
                socketAddress, this.subClientId, packet
            )
        }
    }

    val socketAddress: SocketAddress
        get() = peer.socketAddress

    val isSubClient: Boolean
        get() = this.subClientId != 0

    val isDisconnected: Boolean
        get() = closed.get()

    /**
     * Close Network Session.
     *
     * @param reason the reason,when it is not null,will send a DisconnectPacket to client
     */
    @ApiStatus.Internal
    fun close(reason: String?) {
        if (closed.get()) {
            return
        }

        //when a player haven't login,it only hold a BedrockSession,and Player Instance is null
        if (reason != null) {
            val packet = DisconnectPacket()
            packet.message = reason
            this.sendPacketImmediately(packet)
        }

        Server.instance.scheduler.scheduleDelayedTask(InternalPlugin.INSTANCE, {
            if (isSubClient) {
                // FIXME: Do sub-clients send a server-bound DisconnectPacket?
            } else {
                // Primary sub-client controls the connection
                peer.close()
            }
        }, 5)
    }

    /**
     * Player disconnection process
     *
     *
     * 1.BedrockSession#close -> channel#disconnect -> channelInactive-> BedrockPeer#onClose -> all BedrockSession#onClose -> tickFuture#cancel -> free
     *
     *
     * 2.onRakNetDisconnect -> channel#disconnect -> channelInactive-> BedrockPeer#onClose -> all BedrockSession#onClose -> tickFuture#cancel -> free
     *
     *
     * 3.Player#close -> BedrockSession#close
     */
    fun onClose() {
        if (!closed.compareAndSet(false, true)) {
            return
        }
        val player = this.player
        player?.close(BedrockDisconnectReasons.DISCONNECTED)
        Server.instance.network.onSessionDisconnect(address)
        peer.removeSession(this)
    }

    val isConnected: Boolean
        get() = !closed.get()

    val ping: Long
        get() {
            if (isDisconnected) {
                return -1L
            }
            return peer.ping
        }

    fun onPlayerCreated(player: Player) {
        this.player = player
        Server.instance.onPlayerLogin(address, player)
    }

    fun notifyTerrainReady() {
        log.debug("Sending spawn notification, waiting for spawn response")
        val state = machine.state
        check(state == SessionState.PRE_SPAWN) { "attempt to notifyTerrainReady when the state is " + state.name }
        player!!.doFirstSpawn()
    }

    fun onSessionStartSuccess() {
        log.debug("Waiting for login packet")
    }

    private fun createPlayer(): Player? {
        try {
            val event = PlayerCreationEvent(Player::class.java)
            Server.instance.pluginManager.callEvent(event)
            val constructor = event.playerClass.getConstructor(
                BedrockSession::class.java,
                PlayerInfo::class.java
            )
            return constructor.newInstance(this, this.info)
        } catch (e: Exception) {
            log.error("Failed to create player", e)
        }
        return null
    }

    private fun onServerLoginSuccess() {
        log.debug("Login completed")
        this.sendPlayStatus(PlayStatusPacket.LOGIN_SUCCESS, false)
    }

    private fun onClientSpawned() {
        log.debug("Received spawn response, entering in-game phase")
        player!!.setImmobile(false) //TODO: HACK: we set this during the spawn sequence to prevent the client sending junk movements
    }

    protected fun onServerDeath() {
    }

    protected fun onClientRespawn() {
    }

    fun handleDataPacket(packet: DataPacket) {
        val ev = DataPacketReceiveEvent(player, packet)
        Server.instance.pluginManager.callEvent(ev)

        if (ev.cancelled) return

        if (this.packetHandler == null) return

        val inGamePacketHandler = packetHandler
        if (inGamePacketHandler is InGamePacketHandler) {
            inGamePacketHandler.managerHandle(packet)
        } else {
            packet.handle(this.packetHandler!!)
        }
    }

    fun tick() {
        var packet: DataPacket
        val c = consumer.get()
        if (c != null) {
            while ((inbound.poll().also { packet = it }) != null) {
                c.accept(packet)
            }
        } else {
            inbound.clear()
        }
    }

    val addressString: String
        get() = address!!.address.hostAddress

    fun syncAvailableCommands() {
        val pk = AvailableCommandsPacket()
        val data: MutableMap<String, CommandDataVersions> = HashMap()
        var count = 0
        val commands = Server.instance.commandMap.commands
        synchronized(commands) {
            for (command in commands.values) {
                if (!command.testPermissionSilent(player!!) || !command.isRegistered || command.isServerSideOnly) {
                    continue
                }
                ++count
                val data0 = command.generateCustomCommandData(player!!)
                data[command.name] = data0!!
            }
        }
        if (count > 0) {
            //TODO: structure checking
            pk.commands = data
            this.sendPacket(pk)
        }
    }

    fun syncCraftingData() {
        this.sendRawPacket(ProtocolInfo.CRAFTING_DATA_PACKET, Registries.RECIPE.craftingPacket)
    }

    fun syncCreativeContent() {
        val pk = CreativeContentPacket()

        this.sendPacket(pk)
    }

    fun syncInventory() {
        val player = player
        if (player != null) {
            player.inventory.sendHeldItem(player)
            player.inventory.sendContents(player)
            player.inventory.sendArmorContents(player)
            player.cursorInventory.sendContents(player)
            player.offhandInventory.sendContents(player)
            player.enderChestInventory.sendContents(player)
        }
    }

    fun setEnableClientCommand(enable: Boolean) {
        val pk = SetCommandsEnabledPacket()
        pk.enabled = enable
        this.sendPacket(pk)
        if (enable) {
            this.syncAvailableCommands()
        }
    }

    fun setAuthenticated() {
        authenticated = true
    }

    val server: Server
        get() = Server.instance

    val dataPacketManager: DataPacketManager?
        get() {
            val inGamePacketHandler = this.packetHandler
            return if (inGamePacketHandler != null && inGamePacketHandler is InGamePacketHandler) {
                inGamePacketHandler.manager
            } else {
                null
            }
        }
}
