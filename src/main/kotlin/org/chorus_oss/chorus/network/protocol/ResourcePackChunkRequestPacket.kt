package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.utils.Version

import java.util.*


class ResourcePackChunkRequestPacket : AbstractResourcePackDataPacket() {
    override var packId: UUID? = null
    override var packVersion: Version? = null
    var chunkIndex: Int = 0

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

    companion object : PacketDecoder<ResourcePackChunkRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): ResourcePackChunkRequestPacket {
            val packet = ResourcePackChunkRequestPacket()

            packet.decodePackInfo(byteBuf)
            packet.chunkIndex = byteBuf.readIntLE()

            return packet
        }
    }
}
