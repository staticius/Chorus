package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SetHealthPacket : DataPacket() {
    var health: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.health)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_HEALTH_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
