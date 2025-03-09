package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ChunkRadiusUpdatedPacket : DataPacket() {
    @JvmField
    var radius: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.radius = byteBuf.readVarInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.radius)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CHUNK_RADIUS_UPDATED_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
