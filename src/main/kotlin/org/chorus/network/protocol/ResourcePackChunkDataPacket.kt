package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.utils.Version

import java.util.*

class ResourcePackChunkDataPacket : AbstractResourcePackDataPacket() {
    override var packId: UUID? = null
    override var packVersion: Version? = null
    var chunkIndex: Int = 0
    var progress: Long = 0
    var data: ByteArray = ByteArray(0)

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

    companion object : PacketDecoder<ResourcePackChunkDataPacket> {
        override fun decode(byteBuf: HandleByteBuf): ResourcePackChunkDataPacket {
            val packet = ResourcePackChunkDataPacket()

            packet.decodePackInfo(byteBuf)
            packet.chunkIndex = byteBuf.readIntLE()
            packet.progress = byteBuf.readLongLE()
            packet.data = byteBuf.readByteArray()

            return packet
        }
    }
}
