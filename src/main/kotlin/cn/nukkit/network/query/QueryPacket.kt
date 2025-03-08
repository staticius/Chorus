package cn.nukkit.network.query

import io.netty.buffer.ByteBuf

interface QueryPacket {
    fun encode(buf: ByteBuf)

    fun decode(buf: ByteBuf)

    var sessionId: Int

    val id: Short
}
