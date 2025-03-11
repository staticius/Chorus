package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.registry.Registries






class BiomeDefinitionListPacket : DataPacket() {
    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBytes(Registries.BIOME.biomeDefinitionListPacketData)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.BIOME_DEFINITION_LIST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
