package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.GameType


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
        byteBuf.writeVarInt(gameType!!.ordinal)
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
