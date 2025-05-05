package org.chorus_oss.chorus.network.query.packet

import io.netty.buffer.ByteBuf
import org.chorus_oss.chorus.network.query.QueryPacket
import org.chorus_oss.chorus.network.query.QueryUtil

class HandshakePacket : QueryPacket {
    // Both
    override var sessionId: Int = 0

    // Response
    var token: String? = null

    override fun decode(buf: ByteBuf) {
        sessionId = buf.readInt()
    }

    override fun encode(buf: ByteBuf) {
        buf.writeInt(sessionId)
        QueryUtil.writeNullTerminatedString(buf, token!!)
    }

    override val id: Short
        get() = ID

    companion object {
        const val ID: Short = 0x09
    }
}
