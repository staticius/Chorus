package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.registry.Registries
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
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


