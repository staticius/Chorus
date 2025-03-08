package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString(exclude = ["namedtag"])
@NoArgsConstructor
@AllArgsConstructor
class UpdateEquipmentPacket : DataPacket() {
    var windowId: Int = 0
    var windowType: Int = 0
    var size: Int = 0
    var eid: Long = 0
    var namedtag: ByteArray

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(windowId.toByte().toInt())
        byteBuf.writeByte(windowType.toByte().toInt())
        byteBuf.writeVarInt(size)
        byteBuf.writeEntityUniqueId(this.eid)
        byteBuf.writeBytes(this.namedtag)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.UPDATE_EQUIPMENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
