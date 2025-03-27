package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class ChunkRadiusUpdatedPacket(
    var radius: Int
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.radius)
    }

    override fun pid(): Int {
        return ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<ChunkRadiusUpdatedPacket> {
        override fun decode(byteBuf: HandleByteBuf): ChunkRadiusUpdatedPacket {
            return ChunkRadiusUpdatedPacket(
                radius = byteBuf.readVarInt()
            )
        }
    }
}
