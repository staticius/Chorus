package org.chorus.network.query.packet

import io.netty.buffer.ByteBuf
import org.chorus.network.query.QueryPacket
import org.chorus.network.query.QueryUtil

class HandshakePacket : QueryPacket {
    // Both
    override var sessionId: Int = 0

    // Response
    var token: String? = null

    override fun decode(buffer: ByteBuf) {
        sessionId = buffer.readInt()
    }

    override fun encode(buffer: ByteBuf) {
        buffer.writeInt(sessionId)
        QueryUtil.writeNullTerminatedString(buffer, token!!)
    }

    companion object {
        val id: Short = 0x09
            get() = Companion.field
    }
}
