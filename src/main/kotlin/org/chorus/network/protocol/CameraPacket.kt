package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.ActorUniqueID

data class CameraPacket(
    val cameraID: ActorUniqueID,
    val targetPlayerID: ActorUniqueID,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorUniqueID(this.cameraID)
        byteBuf.writeActorUniqueID(this.targetPlayerID)
    }

    override fun pid(): Int {
        return ProtocolInfo.CAMERA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
