package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class OnScreenTextureAnimationPacket : DataPacket() {
    var effectId: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.effectId = byteBuf.readIntLE()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeIntLE(this.effectId)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ON_SCREEN_TEXTURE_ANIMATION_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
