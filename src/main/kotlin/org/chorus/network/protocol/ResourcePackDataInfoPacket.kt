package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.utils.Version

import java.util.*


(exclude = ["sha256"])


class ResourcePackDataInfoPacket : AbstractResourcePackDataPacket() {
    override var packId: UUID? = null
    override var packVersion: Version? = null
    var maxChunkSize: Int = 0
    var chunkCount: Int = 0
    var compressedPackSize: Long = 0
    var sha256: ByteArray
    var premium: Boolean = false
    var type: Int = TYPE_RESOURCE

    override fun decode(byteBuf: HandleByteBuf) {
        decodePackInfo(byteBuf)
        this.maxChunkSize = byteBuf.readIntLE()
        this.chunkCount = byteBuf.readIntLE()
        this.compressedPackSize = byteBuf.readLongLE()
        this.sha256 = byteBuf.readByteArray()
        this.premium = byteBuf.readBoolean()
        this.type = byteBuf.readByte().toInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        encodePackInfo(byteBuf)
        byteBuf.writeIntLE(this.maxChunkSize)
        byteBuf.writeIntLE(this.chunkCount)
        byteBuf.writeLongLE(this.compressedPackSize)
        byteBuf.writeByteArray(this.sha256)
        byteBuf.writeBoolean(this.premium)
        byteBuf.writeByte(type.toByte().toInt())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.RESOURCE_PACK_DATA_INFO_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val TYPE_INVALID: Int = 0
        const val TYPE_ADDON: Int = 1
        const val TYPE_CACHED: Int = 2
        const val TYPE_COPY_PROTECTED: Int = 3
        const val TYPE_BEHAVIOR: Int = 4
        const val TYPE_PERSONA_PIECE: Int = 5
        const val TYPE_RESOURCE: Int = 6
        const val TYPE_SKINS: Int = 7
        const val TYPE_WORLD_TEMPLATE: Int = 8
        const val TYPE_COUNT: Int = 9
    }
}
