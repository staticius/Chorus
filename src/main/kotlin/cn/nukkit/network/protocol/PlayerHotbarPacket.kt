package cn.nukkit.network.protocol

import cn.nukkit.inventory.SpecialWindowId
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@ToString
@NoArgsConstructor
@AllArgsConstructor
class PlayerHotbarPacket : DataPacket() {
    var selectedHotbarSlot: Int = 0
    var windowId: Int = SpecialWindowId.PLAYER.id
    var selectHotbarSlot: Boolean = true

    override fun decode(byteBuf: HandleByteBuf) {
        this.selectedHotbarSlot = byteBuf.readUnsignedVarInt()
        this.windowId = byteBuf.readByte().toInt()
        this.selectHotbarSlot = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.selectedHotbarSlot)
        byteBuf.writeByte(windowId.toByte().toInt())
        byteBuf.writeBoolean(this.selectHotbarSlot)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAYER_HOTBAR_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
