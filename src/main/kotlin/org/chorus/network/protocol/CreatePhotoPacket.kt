package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf







class CreatePhotoPacket : DataPacket() {
    var id: Long = 0
    var photoName: String? = null
    var photoItemName: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeLongLE(id)
        byteBuf.writeString(photoName!!)
        byteBuf.writeString(photoItemName!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CREATE_PHOTO_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
