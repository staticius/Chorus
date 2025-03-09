package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.registry.Registries
import lombok.*





class AvailableEntityIdentifiersPacket : DataPacket() {
    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBytes(Registries.ENTITY.tag)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.AVAILABLE_ENTITY_IDENTIFIERS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}


