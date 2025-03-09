package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class PlayerInputPacket : DataPacket() {
    var motionX: Float = 0f
    var motionY: Float = 0f
    var jumping: Boolean = false
    var sneaking: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.motionX = byteBuf.readFloatLE()
        this.motionY = byteBuf.readFloatLE()
        this.jumping = byteBuf.readBoolean()
        this.sneaking = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAYER_INPUT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
