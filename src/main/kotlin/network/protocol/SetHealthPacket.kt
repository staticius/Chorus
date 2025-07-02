package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class SetHealthPacket : DataPacket() {
    var health: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.health)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_HEALTH_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
