package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class CreatePhotoPacket(
    val rawID: Long,
    val photoName: String,
    val photoItemName: String,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeLongLE(this.rawID)
        byteBuf.writeString(this.photoName)
        byteBuf.writeString(this.photoItemName)
    }

    override fun pid(): Int {
        return ProtocolInfo.CREATE_PHOTO_PACKET
    }
}
