package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.utils.version.Version
import lombok.*
import java.util.*

@Getter
@Setter
@ToString(exclude = ["data"])
@NoArgsConstructor
@AllArgsConstructor
class ResourcePackChunkDataPacket : AbstractResourcePackDataPacket() {
    override var packId: UUID? = null
    override var packVersion: Version? = null
    var chunkIndex: Int = 0
    var progress: Long = 0
    var data: ByteArray

    override fun decode(byteBuf: HandleByteBuf) {
        decodePackInfo(byteBuf)
        this.chunkIndex = byteBuf.readIntLE()
        this.progress = byteBuf.readLongLE()
        this.data = byteBuf.readByteArray()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        encodePackInfo(byteBuf)
        byteBuf.writeIntLE(this.chunkIndex)
        byteBuf.writeLongLE(this.progress)
        byteBuf.writeByteArray(this.data)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.RESOURCE_PACK_CHUNK_DATA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
