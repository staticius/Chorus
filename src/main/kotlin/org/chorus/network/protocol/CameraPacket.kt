package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class CameraPacket : DataPacket() {
    var cameraUniqueId: Long = 0
    var playerUniqueId: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.cameraUniqueId = byteBuf.readVarLong()
        this.playerUniqueId = byteBuf.readVarLong()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityUniqueId(this.cameraUniqueId)
        byteBuf.writeEntityUniqueId(this.playerUniqueId)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CAMERA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
