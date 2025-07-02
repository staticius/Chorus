package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class ShowProfilePacket : DataPacket() {
    @JvmField
    var xuid: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.xuid = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(xuid!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SHOW_PROFILE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
