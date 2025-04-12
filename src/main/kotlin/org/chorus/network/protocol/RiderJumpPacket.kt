package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class RiderJumpPacket : DataPacket() {
    /**
     * Corresponds to jump progress bars 0-100
     */
    var jumpStrength: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.jumpStrength)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.RIDER_JUMP_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<RiderJumpPacket> {
        override fun decode(byteBuf: HandleByteBuf): RiderJumpPacket {
            val packet = RiderJumpPacket()

            packet.jumpStrength = byteBuf.readVarInt()

            return packet
        }
    }
}
