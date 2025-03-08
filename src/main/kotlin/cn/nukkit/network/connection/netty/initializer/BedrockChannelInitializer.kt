package cn.nukkit.network.connection.netty.initializer

import cn.nukkit.compression.CompressionProvider
import cn.nukkit.network.connection.BedrockPeer
import cn.nukkit.network.connection.BedrockSession
import cn.nukkit.network.connection.netty.codec.FrameIdCodec
import cn.nukkit.network.connection.netty.codec.batch.BedrockBatchDecoder
import cn.nukkit.network.connection.netty.codec.batch.BedrockBatchEncoder
import cn.nukkit.network.connection.netty.codec.compression.*
import cn.nukkit.network.connection.netty.codec.packet.BedrockPacketCodec
import cn.nukkit.network.connection.netty.codec.packet.BedrockPacketCodec_v1
import cn.nukkit.network.connection.netty.codec.packet.BedrockPacketCodec_v2
import cn.nukkit.network.connection.netty.codec.packet.BedrockPacketCodec_v3
import cn.nukkit.network.protocol.types.*
import io.netty.channel.*
import lombok.extern.slf4j.Slf4j
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption

@Slf4j
abstract class BedrockChannelInitializer<T : BedrockSession?> : ChannelInitializer<Channel>() {
    @Throws(Exception::class)
    override fun initChannel(channel: Channel) {
        // Decode
        // RAKNET_FRAME_CODEC -> CompressionCodec -> BATCH_DECODER ->  BedrockPacketCodec -> BedrockPeer
        // Encode
        // BedrockPeer -> BedrockPacketCodec-> BATCH_ENCODER -> CompressionCodec -> RAKNET_FRAME_CODEC
        this.preInitChannel(channel)

        channel.pipeline()
            .addLast(BedrockBatchDecoder.Companion.NAME, BATCH_DECODER)
            .addLast(BedrockBatchEncoder.Companion.NAME, BedrockBatchEncoder())

        this.initPacketCodec(channel)

        channel.pipeline().addLast(BedrockPeer.Companion.NAME, this.createPeer(channel))

        this.postInitChannel(channel)
    }

    protected fun preInitChannel(channel: Channel) {
        channel.pipeline().addLast(FrameIdCodec.Companion.NAME, RAKNET_FRAME_CODEC)

        val rakVersion = channel.config().getOption(RakChannelOption.RAK_PROTOCOL_VERSION)

        val compression = getCompression(PacketCompressionAlgorithm.ZLIB, rakVersion, true)
        // At this point all connections use not prefixed compression
        channel.pipeline().addLast(CompressionCodec.Companion.NAME, CompressionCodec(compression, false))
    }

    @Throws(Exception::class)
    protected open fun postInitChannel(channel: Channel?) {
    }

    @Throws(Exception::class)
    protected fun initPacketCodec(channel: Channel) {
        val rakVersion = channel.config().getOption(RakChannelOption.RAK_PROTOCOL_VERSION)

        when (rakVersion) {
            11, 10, 9 -> channel.pipeline().addLast(BedrockPacketCodec.Companion.NAME, BedrockPacketCodec_v3())
            8 -> channel.pipeline().addLast(BedrockPacketCodec.Companion.NAME, BedrockPacketCodec_v2())
            7 -> channel.pipeline().addLast(BedrockPacketCodec.Companion.NAME, BedrockPacketCodec_v1())
            else -> throw UnsupportedOperationException("Unsupported RakNet protocol version: $rakVersion")
        }
    }

    protected open fun createPeer(channel: Channel): BedrockPeer? {
        return BedrockPeer(
            channel
        ) { peer: BedrockPeer?, subClientId: Int -> this.createSession(peer, subClientId) }
    }

    protected fun createSession(peer: BedrockPeer?, subClientId: Int): T {
        val session = this.createSession0(peer, subClientId)
        this.initSession(session)
        return session
    }

    protected abstract fun createSession0(peer: BedrockPeer?, subClientId: Int): T

    protected abstract fun initSession(session: T)

    companion object {
        const val RAKNET_MINECRAFT_ID: Int = 0xFE
        private val RAKNET_FRAME_CODEC = FrameIdCodec(RAKNET_MINECRAFT_ID)
        private val BATCH_DECODER = BedrockBatchDecoder()

        private val ZLIB_RAW_STRATEGY: CompressionStrategy =
            SimpleCompressionStrategy(ZlibCompression(CompressionProvider.ZLIB_RAW))
        private val ZLIB_STRATEGY: CompressionStrategy =
            SimpleCompressionStrategy(ZlibCompression(CompressionProvider.ZLIB))
        private val SNAPPY_STRATEGY: CompressionStrategy = SimpleCompressionStrategy(SnappyCompression())
        private val NOOP_STRATEGY: CompressionStrategy = SimpleCompressionStrategy(NoopCompression())

        fun getCompression(algorithm: CompressionAlgorithm, rakVersion: Int, initial: Boolean): CompressionStrategy {
            return when (rakVersion) {
                7, 8, 9 -> ZLIB_STRATEGY
                10 -> ZLIB_RAW_STRATEGY
                11 -> if (initial) NOOP_STRATEGY else getCompression(algorithm)
                else -> throw UnsupportedOperationException("Unsupported RakNet protocol version: $rakVersion")
            }
        }

        private fun getCompression(algorithm: CompressionAlgorithm): CompressionStrategy {
            return when (algorithm) {
                PacketCompressionAlgorithm.ZLIB -> ZLIB_RAW_STRATEGY
                PacketCompressionAlgorithm.SNAPPY -> SNAPPY_STRATEGY
                PacketCompressionAlgorithm.NONE -> NOOP_STRATEGY
                else -> throw UnsupportedOperationException("Unsupported compression algorithm: $algorithm")
            }
        }
    }
}
