package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.utils.version.Version
import lombok.*
import java.util.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ResourcePackChunkRequestPacket : AbstractResourcePackDataPacket() {
    override var packId: UUID? = null
    override var packVersion: Version? = null
    var chunkIndex: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        decodePackInfo(byteBuf)
        this.chunkIndex = byteBuf.readIntLE()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        encodePackInfo(byteBuf)
        byteBuf.writeIntLE(this.chunkIndex)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.RESOURCE_PACK_CHUNK_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
