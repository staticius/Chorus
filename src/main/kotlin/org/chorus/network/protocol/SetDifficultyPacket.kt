package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class SetDifficultyPacket : DataPacket() {
    var difficulty: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.difficulty = byteBuf.readUnsignedVarInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.difficulty)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_DIFFICULTY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
