package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf







class SetHealthPacket : DataPacket() {
    var health: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

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
