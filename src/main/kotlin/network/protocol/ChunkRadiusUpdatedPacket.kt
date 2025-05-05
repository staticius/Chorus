package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class ChunkRadiusUpdatedPacket(
    val radius: Int
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
}
