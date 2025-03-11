package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf






class EntityPickRequestPacket : DataPacket() {
    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        //TODO: Implement
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ENTITY_PICK_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
