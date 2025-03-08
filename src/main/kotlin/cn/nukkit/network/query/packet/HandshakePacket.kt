package cn.nukkit.network.query.packet

import cn.nukkit.network.query.QueryPacket
import cn.nukkit.network.query.QueryUtil
import io.netty.buffer.ByteBuf

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
