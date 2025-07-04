package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.GameType


class UpdatePlayerGameTypePacket : DataPacket() {
    @JvmField
    var gameType: GameType? = null

    @JvmField
    var entityId: Long = 0
    var tick: Long = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(gameType!!.ordinal)
        byteBuf.writeVarLong(entityId)
        byteBuf.writeUnsignedVarLong(tick)
    }

    override fun pid(): Int {
        return ProtocolInfo.UPDATE_PLAYER_GAME_TYPE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<UpdatePlayerGameTypePacket> {
        override fun decode(byteBuf: HandleByteBuf): UpdatePlayerGameTypePacket {
            val packet = UpdatePlayerGameTypePacket()

            packet.gameType = GameType.from(byteBuf.readVarInt())
            packet.entityId = byteBuf.readVarLong()
            packet.tick = byteBuf.readUnsignedVarLong()

            return packet
        }
    }
}
