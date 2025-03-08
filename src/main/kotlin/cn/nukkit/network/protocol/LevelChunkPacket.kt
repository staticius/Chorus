package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
@Getter
@Setter
@ToString(exclude = ["data"])
@NoArgsConstructor
@AllArgsConstructor
class LevelChunkPacket : DataPacket() {
    @JvmField
    var chunkX: Int = 0

    @JvmField
    var chunkZ: Int = 0
    @JvmField
    var subChunkCount: Int = 0
    var cacheEnabled: Boolean = false
    var requestSubChunks: Boolean = false
    var subChunkLimit: Int = 0
    var blobIds: LongArray
    @JvmField
    var data: ByteArray

    /**
     * @since v649
     */
    @JvmField
    var dimension: Int = 0
    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.chunkX)
        byteBuf.writeVarInt(this.chunkZ)
        byteBuf.writeVarInt(this.dimension)
        if (!this.requestSubChunks) {
            byteBuf.writeUnsignedVarInt(this.subChunkCount)
        } else if (this.subChunkLimit < 0) {
            byteBuf.writeUnsignedVarInt(-1)
        } else {
            byteBuf.writeUnsignedVarInt(-2)
            byteBuf.writeUnsignedVarInt(this.subChunkLimit)
        }
        byteBuf.writeBoolean(cacheEnabled)
        if (this.cacheEnabled) {
            byteBuf.writeUnsignedVarInt(blobIds.size)

            for (blobId in blobIds) {
                byteBuf.writeLongLE(blobId)
            }
        }
        byteBuf.writeByteArray(this.data)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.FULL_CHUNK_DATA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
