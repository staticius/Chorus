package org.chorus.network.connection.netty.codec.compression

import org.chorus.network.protocol.types.*
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import lombok.*

@ToString
class NoopCompression : BatchCompression {
    @Throws(Exception::class)
    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf): ByteBuf? {
        return msg.retainedSlice()
    }

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf): ByteBuf? {
        return msg.retainedSlice()
    }

    override val algorithm: CompressionAlgorithm
        get() = PacketCompressionAlgorithm.NONE

    override var level: Int
        get() = -1
        set(level) {
        }
}
