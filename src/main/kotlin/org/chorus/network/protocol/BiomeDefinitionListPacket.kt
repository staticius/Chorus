package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.registry.Registries
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
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
