package org.chorus.network

import org.chorus.Player
import org.chorus.Server
import org.chorus.config.ServerPropertiesKeys
import org.chorus.network.connection.BedrockPeer
import org.chorus.network.connection.BedrockPong
import org.chorus.network.connection.BedrockSession
import org.chorus.network.connection.netty.initializer.BedrockServerInitializer
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.query.codec.QueryPacketCodec
import org.chorus.network.query.handler.QueryPacketHandler
import org.chorus.plugin.InternalPlugin
import org.chorus.utils.Utils
import com.google.common.base.Strings
import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollDatagramChannel
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.kqueue.KQueue
import io.netty.channel.kqueue.KQueueDatagramChannel
import io.netty.channel.kqueue.KQueueEventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.nio.NioDatagramChannel
import lombok.extern.slf4j.Slf4j
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory
import org.cloudburstmc.netty.channel.raknet.RakServerChannel
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption
import oshi.SystemInfo
import oshi.hardware.NetworkIF
import java.net.InetAddress
import java.net.InetSocketAddress
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicReference

/**
 * @author MagicDroidX (Nukkit Project)
 */

class Network @JvmOverloads constructor(
    val server: Server,
    nettyThreadNumber: Int = Runtime.getRuntime().availableProcessors(),
    threadFactory: ThreadFactory? = ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").build()
) {
    private val netWorkStatisticDataList = LinkedList<NetWorkStatisticData>()
    private val hardWareNetworkInterfaces = AtomicReference<List<NetworkIF>?>(null)
    private val sessionMap: MutableMap<InetSocketAddress?, BedrockSession> = ConcurrentHashMap()
    private val blockIpMap: MutableMap<InetAddress, LocalDateTime> = HashMap()
    private val channel: RakServerChannel

    /**
     * Gets raknet pong.
     */
    val pong: BedrockPong = BedrockPong(
        null,
        "MCPE",
        server.motd,
        server.subMotd,
        server.onlinePlayers.size,
        server.getMaxPlayers(),
        server.serverID.mostSignificantBits,
        Server.getGamemodeString(server.defaultGamemode, true)
        false,
        ProtocolInfo.CURRENT_PROTOCOL,
        null,
        server.port,
        server.port
    )

    init {
        server.scheduler.scheduleTask(InternalPlugin.INSTANCE, {
            var tmpIfs: List<NetworkIF>? = null
            try {
                tmpIfs = SystemInfo().hardware.networkIFs
            } catch (t: Throwable) {
                Network.log.warn(Server.getInstance().language.get("nukkit.start.hardwareMonitorDisabled"))
            }
            hardWareNetworkInterfaces.set(tmpIfs)
        }, true)

        val oclass: Class<out DatagramChannel?>
        val eventloopgroup: EventLoopGroup
        if (Epoll.isAvailable()) {
            oclass = EpollDatagramChannel::class.java
            eventloopgroup = EpollEventLoopGroup(nettyThreadNumber, threadFactory)
        } else if (KQueue.isAvailable()) {
            oclass = KQueueDatagramChannel::class.java
            eventloopgroup = KQueueEventLoopGroup(nettyThreadNumber, threadFactory)
        } else {
            oclass = NioDatagramChannel::class.java
            eventloopgroup = NioEventLoopGroup(nettyThreadNumber, threadFactory)
        }
        val bindAddress = InetSocketAddress(
            if (Strings.isNullOrEmpty(server.ip)) "0.0.0.0" else server.ip,
            server.port
        )

        this.channel = ServerBootstrap()
            .channelFactory(RakChannelFactory.server(oclass))
            .option<ByteBuf>(RakChannelOption.RAK_ADVERTISEMENT, pong!!.toByteBuf())
            .option(RakChannelOption.RAK_PACKET_LIMIT, server.settings.networkSettings.packetLimit)
            .option<Boolean>(RakChannelOption.RAK_SEND_COOKIE, true)
            .group(eventloopgroup)
            .childHandler(object : BedrockServerInitializer() {
                override fun postInitChannel(channel: Channel?) {
                    if (server.properties.get(ServerPropertiesKeys.ENABLE_QUERY, true)) {
                        channel!!.pipeline().addLast("queryPacketCodec", QueryPacketCodec())
                            .addLast(
                                "queryPacketHandler",
                                QueryPacketHandler { address: InetSocketAddress? -> server.queryInformation })
                    }
                }

                override fun createPeer(channel: Channel): BedrockPeer? {
                    return super.createPeer(channel)
                }

                override fun createSession0(peer: BedrockPeer?, subClientId: Int): BedrockSession {
                    val session = BedrockSession(peer, subClientId)
                    val address = session.socketAddress as InetSocketAddress?
                    if (isAddressBlocked(address!!)) {
                        session.close("Your IP address has been blocked by this server!")
                        onSessionDisconnect(address)
                    } else {
                        sessionMap[address] = session
                    }
                    return session
                }

                override fun initSession(session: BedrockSession) {}
            })
            .bind(bindAddress)
            .awaitUninterruptibly()
            .channel() as RakServerChannel
        pong!!.channel = channel
    }

    @JvmRecord
    internal data class NetWorkStatisticData(val upload: Long, val download: Long)

    fun shutdown() {
        channel.close()
        this.pong = null
        sessionMap.clear()
        netWorkStatisticDataList.clear()
    }

    val upload: Double
        get() = (netWorkStatisticDataList[1].upload - netWorkStatisticDataList[0].upload).toDouble()

    val download: Double
        get() = (netWorkStatisticDataList[1].download - netWorkStatisticDataList[0].download).toDouble()

    fun resetStatistics() {
        var upload: Long = 0
        var download: Long = 0
        if (netWorkStatisticDataList.size > 1) {
            netWorkStatisticDataList.removeFirst()
        }
        if (this.getHardWareNetworkInterfaces() != null) {
            for (networkIF in getHardWareNetworkInterfaces()!!) {
                networkIF.updateAttributes()
                upload += networkIF.bytesSent
                download += networkIF.bytesRecv
            }
        }
        netWorkStatisticDataList.add(NetWorkStatisticData(upload, download))
    }

    /**
     * process tick for all network interfaces.
     */
    fun processInterfaces() {
        try {
            this.process()
        } catch (e: Exception) {
            Network.log.error(
                server.language.tr(
                    "nukkit.server.networkError",
                    javaClass.name, Utils.getExceptionMessage(e)
                ), e
            )
        }
    }

    fun getHardWareNetworkInterfaces(): List<NetworkIF>? {
        return hardWareNetworkInterfaces.get()
    }


    /**
     * Get network latency for specific player.
     *
     * @param player the player
     * @return the network latency
     */
    fun getNetworkLatency(player: Player): Int {
        val session = sessionMap[player.rawSocketAddress]
        return session?.ping?.toInt() ?: -1
    }

    /**
     * Block an address forever.
     *
     * @param address the address
     */
    fun blockAddress(address: InetAddress) {
        blockIpMap[address] = LocalDateTime.of(9999, 1, 1, 0, 0)
    }

    /**
     * Block an address.
     *
     * @param address the address
     * @param timeout the timeout,unit millisecond
     */
    fun blockAddress(address: InetAddress, timeout: Int) {
        blockIpMap[address] = LocalDateTime.now().plus(timeout.toLong(), ChronoUnit.MILLIS)
    }

    /**
     * Recover an address of banned.
     *
     * @param address the address
     */
    fun unblockAddress(address: InetAddress) {
        blockIpMap.remove(address)
    }

    /**
     * Get a session of player.
     *
     * @param address the address of session
     * @return the session
     */
    fun getSession(address: InetSocketAddress?): BedrockSession? {
        return sessionMap[address]
    }

    /**
     * Replace session address.
     *
     *
     * handle a scenario that the player from proxy
     *
     * @param oldAddress the old address
     * @param newAddress the new address,usually the IP of the proxy
     * @param newSession original session
     */
    fun replaceSessionAddress(
        oldAddress: InetSocketAddress?,
        newAddress: InetSocketAddress,
        newSession: BedrockSession
    ) {
        if (!sessionMap.containsKey(oldAddress)) return

        if (isAddressBlocked(newAddress)) return

        onSessionDisconnect(oldAddress)
        sessionMap[newAddress] = newSession
    }

    /**
     * whether the address is blocked
     */
    fun isAddressBlocked(address: InetSocketAddress): Boolean {
        val a = address.address
        if (blockIpMap.containsKey(a)) {
            val localDateTime = blockIpMap[a]
            return LocalDateTime.now().isBefore(localDateTime)
        }
        return false
    }

    /**
     * A function of tick for network session
     */
    fun process() {
        sessionMap.values.stream().filter { session: BedrockSession -> session.isConnected && session.player == null }
            .forEach { obj: BedrockSession -> obj.tick() }
    }

    /**
     * call on session disconnect.
     */
    fun onSessionDisconnect(address: InetSocketAddress?) {
        sessionMap.remove(address)
    }
}
