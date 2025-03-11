package org.chorus.network.connection

import org.chorus.network.connection.netty.BedrockPacketWrapper
import org.chorus.network.connection.netty.codec.FrameIdCodec
import org.chorus.network.connection.netty.codec.batch.BedrockBatchDecoder
import org.chorus.network.connection.netty.codec.compression.CompressionCodec
import org.chorus.network.connection.netty.codec.compression.CompressionStrategy
import org.chorus.network.connection.netty.codec.encryption.BedrockEncryptionDecoder
import org.chorus.network.connection.netty.codec.encryption.BedrockEncryptionEncoder
import org.chorus.network.connection.netty.initializer.BedrockChannelInitializer
import org.chorus.network.connection.util.EncryptionUtils
import org.chorus.network.protocol.DataPacket
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.types.PacketCompressionAlgorithm
import io.netty.channel.*
import io.netty.handler.codec.*
import io.netty.util.ReferenceCountUtil
import io.netty.util.concurrent.ScheduledFuture
import io.netty.util.internal.PlatformDependent
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

import org.cloudburstmc.netty.channel.raknet.RakDisconnectReason
import org.cloudburstmc.netty.channel.raknet.RakServerChannel
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption
import org.cloudburstmc.netty.handler.codec.raknet.common.RakSessionCodec
import org.jetbrains.annotations.ApiStatus
import java.net.SocketAddress
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.crypto.SecretKey

/**
 * A Bedrock peer that represents a single network connection to the remote peer.
 * It can hold one or more [BedrockSession]s.
 */

class BedrockPeer(val channel: Channel, protected val sessionFactory: BedrockSessionFactory) :
    ChannelInboundHandlerAdapter() {
    protected val sessions: Int2ObjectMap<BedrockSession> = Int2ObjectOpenHashMap()
    protected val packetQueue: Queue<BedrockPacketWrapper> = PlatformDependent.newMpscQueue()
    protected var tickFuture: ScheduledFuture<*>? = null
    protected var closed: AtomicBoolean = AtomicBoolean()

    protected fun onBedrockPacket(wrapper: BedrockPacketWrapper) {
        val targetId = wrapper.targetSubClientId
        val session = sessions.computeIfAbsent(
            targetId,
            Int2ObjectFunction { sessionId: Int -> this.onSessionCreated(sessionId) })
        session.onPacket(wrapper)
    }

    protected fun onSessionCreated(sessionId: Int): BedrockSession? {
        return sessionFactory.createSession(this, sessionId)
    }

    protected fun checkForClosed() {
        check(!closed.get()) { "Peer has been closed" }
    }

    fun removeSession(session: BedrockSession) {
        sessions.remove(session.subClientId, session)
    }

    protected fun onTick() {
        if (closed.get()) {
            return
        }

        this.flushSendQueue()
    }

    fun flushSendQueue() {
        if (!packetQueue.isEmpty()) {
            var packet: BedrockPacketWrapper?
            while ((packetQueue.poll().also { packet = it }) != null) {
                if (this.isConnected) {
                    channel.write(packet)
                }
            }
            channel.flush()
        }
    }

    private fun onRakNetDisconnect(ctx: ChannelHandlerContext, reason: RakDisconnectReason) {
        val disconnectReason = BedrockDisconnectReasons.getReason(reason)
        for (session in sessions.values) {
            session.close(disconnectReason)
        }
    }

    private fun free() {
        for (wrapper in this.packetQueue) {
            ReferenceCountUtil.safeRelease(wrapper)
        }
    }

    /**
     * Send packet Asynchronously.
     *
     * @param senderClientId the sender client id
     * @param targetClientId the target client id
     * @param packet         the packet
     */
    fun sendPacket(senderClientId: Int, targetClientId: Int, packet: DataPacket?) {
        packetQueue.add(BedrockPacketWrapper(0, senderClientId, targetClientId, packet, null))
    }

    fun sendPacketSync(senderClientId: Int, targetClientId: Int, packet: DataPacket?) {
        channel.writeAndFlush(BedrockPacketWrapper(0, senderClientId, targetClientId, packet, null))
            .syncUninterruptibly()
    }

    /**
     * Send packet immediately Asynchronously.
     *
     * @param senderClientId the sender client id
     * @param targetClientId the target client id
     * @param packet         the packet
     */
    fun sendPacketImmediately(senderClientId: Int, targetClientId: Int, packet: DataPacket?) {
        channel.writeAndFlush(BedrockPacketWrapper(0, senderClientId, targetClientId, packet, null))
    }

    fun sendRawPacket(packet: BedrockPacketWrapper?) {
        channel.writeAndFlush(packet)
    }

    fun flush() {
        channel.flush()
    }

    fun enableEncryption(secretKey: SecretKey) {
        Objects.requireNonNull(secretKey, "secretKey")
        require(secretKey.algorithm == "AES") { "Invalid key algorithm" }
        // Check if the codecs exist in the pipeline
        check(
            !(channel.pipeline().get(BedrockEncryptionEncoder::class.java) != null ||
                    channel.pipeline().get(BedrockEncryptionDecoder::class.java) != null)
        ) { "Encryption is already enabled" }

        val protocolVersion = ProtocolInfo.CURRENT_PROTOCOL
        val useCtr = protocolVersion >= 428

        channel.pipeline().addAfter(
            FrameIdCodec.Companion.NAME, BedrockEncryptionEncoder.Companion.NAME,
            BedrockEncryptionEncoder(secretKey, EncryptionUtils.createCipher(useCtr, true, secretKey))
        )
        channel.pipeline().addAfter(
            FrameIdCodec.Companion.NAME, BedrockEncryptionDecoder.Companion.NAME,
            BedrockEncryptionDecoder(secretKey, EncryptionUtils.createCipher(useCtr, false, secretKey))
        )

        BedrockPeer.log.debug("Encryption enabled for {}", socketAddress)
    }

    fun setCompression(algorithm: PacketCompressionAlgorithm) {
        Objects.requireNonNull(algorithm, "algorithm")
        this.setCompression(
            BedrockChannelInitializer.Companion.getCompression(
                algorithm,
                rakVersion, false
            )
        )
    }

    fun setCompression(strategy: CompressionStrategy) {
        Objects.requireNonNull(strategy, "strategy")

        val needsPrefix = ProtocolInfo.CURRENT_PROTOCOL >= 649 // TODO: do not hardcode

        val handler = channel.pipeline()[CompressionCodec.Companion.NAME]
        if (handler == null) {
            channel.pipeline().addBefore(
                BedrockBatchDecoder.Companion.NAME,
                CompressionCodec.Companion.NAME,
                CompressionCodec(strategy, needsPrefix)
            )
        } else {
            channel.pipeline().replace(
                CompressionCodec.Companion.NAME,
                CompressionCodec.Companion.NAME,
                CompressionCodec(strategy, needsPrefix)
            )
        }
    }

    val compressionStrategy: CompressionStrategy?
        get() {
            val handler =
                channel.pipeline()[CompressionCodec.Companion.NAME] as? CompressionCodec ?: return null
            return handler.strategy
        }

    @ApiStatus.Internal
    fun close() {
        channel.disconnect()
    }

    protected fun onClose() {
        if (channel.isOpen) {
            BedrockPeer.log.warn("Tried to close peer, but channel is open!", Throwable())
            return
        }

        for (session in sessions.values) {
            try {
                session.onClose()
            } catch (e: Exception) {
                BedrockPeer.log.error("Exception whilst closing session", e)
            }
        }

        if (!closed.compareAndSet(false, true)) {
            return
        }

        if (this.tickFuture != null) {
            tickFuture!!.cancel(false)
            this.tickFuture = null
        }

        this.free()
    }

    val isConnected: Boolean
        get() = !closed.get() && channel.isOpen

    val isConnecting: Boolean
        get() = !channel.isActive && !closed.get()

    val socketAddress: SocketAddress
        get() = channel.remoteAddress()

    val rakVersion: Int
        get() = channel.config().getOption(RakChannelOption.RAK_PROTOCOL_VERSION)

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        sessions.put(0, sessionFactory.createSession(this, 0))
        this.tickFuture =
            channel.eventLoop().scheduleAtFixedRate({ this.onTick() }, 10, 10, TimeUnit.MILLISECONDS)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        this.onClose()
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            if (msg is BedrockPacketWrapper) {
                this.onBedrockPacket(msg)
            } else {
                throw DecoderException("Unexpected message type: " + msg.javaClass.name)
            }
        } finally {
            ReferenceCountUtil.release(msg)
        }
    }

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is RakDisconnectReason) {
            onRakNetDisconnect(ctx, evt)
        }
    }

    val ping: Long
        get() {
            val rakServerChannel = channel.parent() as RakServerChannel
            val childChannel = rakServerChannel.getChildChannel(socketAddress)
            val rakSessionCodec =
                childChannel.rakPipeline().get(RakSessionCodec::class.java)
            return rakSessionCodec.ping
        }

    companion object {
        const val NAME: String = "bedrock-peer"
    }
}
