package org.chorus.network.query.packet

import org.chorus.network.query.QueryPacket
import io.netty.buffer.ByteBuf

class StatisticsPacket : QueryPacket {
    // Both
    override var sessionId: Int = 0

    // Request
    var token: Int = 0
    var isFull: Boolean = false

    // Response
    var payload: ByteBuf? = null

    override fun decode(buffer: ByteBuf) {
        sessionId = buffer.readInt()
        token = buffer.readInt()
        isFull = (buffer.isReadable)
        buffer.skipBytes(buffer.readableBytes())
    }

    override fun encode(buffer: ByteBuf) {
        buffer.writeInt(sessionId)
        buffer.writeBytes(payload)
    }

    companion object {
        val id: Short = 0x00
            get() = Companion.field
    }
}
