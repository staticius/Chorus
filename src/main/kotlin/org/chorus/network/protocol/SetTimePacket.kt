package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SetTimePacket : DataPacket() {
    @JvmField
    var time: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.time)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_TIME_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
