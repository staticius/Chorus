package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class RemoveVolumeEntityPacket : DataPacket() {
    var id: Long = 0

    /**
     * @since v503
     */
    var dimension: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        id = byteBuf.readUnsignedVarInt().toLong()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(id.toInt())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REMOVE_VOLUME_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
