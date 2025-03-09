package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*







class SetDefaultGameTypePacket : DataPacket() {
    var gamemode: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.gamemode = byteBuf.readUnsignedVarInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.gamemode)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_DEFAULT_GAME_TYPE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
