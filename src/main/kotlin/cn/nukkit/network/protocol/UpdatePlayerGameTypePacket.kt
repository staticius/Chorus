package cn.nukkit.network.protocol

import cn.nukkit.inventory.InventoryType.Companion.from
import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.GameType
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class UpdatePlayerGameTypePacket : DataPacket() {
    @JvmField
    var gameType: GameType? = null
    @JvmField
    var entityId: Long = 0
    var tick: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.gameType = GameType.Companion.from(byteBuf.readVarInt())
        this.entityId = byteBuf.readVarLong()
        this.tick = byteBuf.readUnsignedVarLong()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(gameType!!.ordinal())
        byteBuf.writeVarLong(entityId)
        byteBuf.writeUnsignedVarLong(tick)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.UPDATE_PLAYER_GAME_TYPE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
