package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class OnScreenTextureAnimationPacket : DataPacket() {
    var effectId: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeIntLE(this.effectId)
    }

    override fun pid(): Int {
        return ProtocolInfo.ON_SCREEN_TEXTURE_ANIMATION_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<OnScreenTextureAnimationPacket> {
        override fun decode(byteBuf: HandleByteBuf): OnScreenTextureAnimationPacket {
            val packet = OnScreenTextureAnimationPacket()

            packet.effectId = byteBuf.readIntLE()

            return packet
        }
    }
}
