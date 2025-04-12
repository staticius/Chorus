package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf

class SetDifficultyPacket : DataPacket() {
    var difficulty: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.difficulty)
    }

    override fun pid(): Int {
        return ProtocolInfo.SET_DIFFICULTY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<SetDifficultyPacket> {
        override fun decode(byteBuf: HandleByteBuf): SetDifficultyPacket {
            val packet = SetDifficultyPacket()

            packet.difficulty = byteBuf.readUnsignedVarInt()

            return packet
        }
    }
}
