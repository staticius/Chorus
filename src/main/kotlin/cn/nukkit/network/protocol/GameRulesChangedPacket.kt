package cn.nukkit.network.protocol

import cn.nukkit.level.GameRules
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class GameRulesChangedPacket : DataPacket() {
    @JvmField
    var gameRules: GameRules? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeGameRules(gameRules!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.GAME_RULES_CHANGED_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
