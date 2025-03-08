package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class PlayerStartItemCoolDownPacket : DataPacket() {
    @JvmField
    var itemCategory: String? = null
    @JvmField
    var coolDownDuration: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.itemCategory = byteBuf.readString()
        this.coolDownDuration = byteBuf.readVarInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(itemCategory!!)
        byteBuf.writeVarInt(coolDownDuration)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAYER_START_ITEM_COOL_DOWN_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
