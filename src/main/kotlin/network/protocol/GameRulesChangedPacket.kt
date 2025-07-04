package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.level.GameRules
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class GameRulesChangedPacket : DataPacket() {
    @JvmField
    var gameRules: GameRules? = null

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeGameRules(gameRules!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.GAME_RULES_CHANGED_PACKET
    }
}
