package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class SetLastHurtByPacket : DataPacket() {
    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        //TODO: Implement
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_LAST_HURT_BY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
