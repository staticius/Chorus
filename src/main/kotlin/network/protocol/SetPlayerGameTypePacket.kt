package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class SetPlayerGameTypePacket : DataPacket() {
    @JvmField
    var gamemode: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.gamemode)
    }

    override fun pid(): Int {
        return ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<SetPlayerGameTypePacket> {
        override fun decode(byteBuf: HandleByteBuf): SetPlayerGameTypePacket {
            val packet = SetPlayerGameTypePacket()

            packet.gamemode = byteBuf.readVarInt()

            return packet
        }
    }
}
