package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class SetDefaultGameTypePacket : DataPacket() {
    var gamemode: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.gamemode)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_DEFAULT_GAME_TYPE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<SetDefaultGameTypePacket> {
        override fun decode(byteBuf: HandleByteBuf): SetDefaultGameTypePacket {
            val packet = SetDefaultGameTypePacket()

            packet.gamemode = byteBuf.readUnsignedVarInt()

            return packet
        }
    }
}
