package org.chorus.network.query.packet

import io.netty.buffer.ByteBuf
import org.chorus.network.query.QueryPacket

class StatisticsPacket : QueryPacket {
    // Both
    override var sessionId: Int = 0

    // Request
    var token: Int = 0
    var isFull: Boolean = false

    // Response
    var payload: ByteBuf? = null

    override fun decode(buf: ByteBuf) {
        sessionId = buf.readInt()
        token = buf.readInt()
        isFull = (buf.isReadable)
        buf.skipBytes(buf.readableBytes())
    }

    override fun encode(buf: ByteBuf) {
        buf.writeInt(sessionId)
        buf.writeBytes(payload)
    }

    override val id: Short
        get() = ID

    companion object {
        const val ID: Short = 0x00
    }
}
