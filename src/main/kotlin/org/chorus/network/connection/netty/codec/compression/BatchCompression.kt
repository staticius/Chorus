package org.chorus.network.connection.netty.codec.compression

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import org.chorus.network.protocol.types.*

interface BatchCompression {
    @Throws(Exception::class)
    fun encode(ctx: ChannelHandlerContext, msg: ByteBuf): ByteBuf?

    @Throws(Exception::class)
    fun decode(ctx: ChannelHandlerContext, msg: ByteBuf): ByteBuf?

    val algorithm: CompressionAlgorithm

    var level: Int
}
