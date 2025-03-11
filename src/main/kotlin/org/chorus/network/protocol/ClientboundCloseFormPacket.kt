package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf






class ClientboundCloseFormPacket : DataPacket() {
    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CHANGE_DIMENSION_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
