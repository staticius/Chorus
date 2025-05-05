package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.level.GameRules
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class GameRulesChangedPacket : DataPacket() {
    @JvmField
    var gameRules: GameRules? = null

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
