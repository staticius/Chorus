package cn.nukkit.network.protocol

import cn.nukkit.math.BlockVector3
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class NetworkChunkPublisherUpdatePacket : DataPacket() {
    var position: BlockVector3? = null
    var radius: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.position = byteBuf.readSignedBlockPosition()
        this.radius = byteBuf.readUnsignedVarInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeSignedBlockPosition(position!!)
        byteBuf.writeUnsignedVarInt(radius)
        byteBuf.writeInt(0) // Saved chunks
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
