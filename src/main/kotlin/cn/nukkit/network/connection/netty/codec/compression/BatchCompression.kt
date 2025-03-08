package cn.nukkit.network.connection.netty.codec.compression

import cn.nukkit.network.protocol.types.*
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

interface BatchCompression {
    @Throws(Exception::class)
    fun encode(ctx: ChannelHandlerContext, msg: ByteBuf): ByteBuf?

    @Throws(Exception::class)
    fun decode(ctx: ChannelHandlerContext, msg: ByteBuf): ByteBuf?

    val algorithm: CompressionAlgorithm

    var level: Int
}
