package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SetPlayerGameTypePacket : DataPacket() {
    @JvmField
    var gamemode: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.gamemode = byteBuf.readVarInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.gamemode)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_PLAYER_GAME_TYPE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
