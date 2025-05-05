package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.math.BlockVector3
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.ActorUniqueID

data class ContainerOpenPacket(
    val containerID: Int,
    val containerType: Int,
    val position: BlockVector3,
    val targetActorID: ActorUniqueID,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(containerID.toByte().toInt())
        byteBuf.writeByte(containerType.toByte().toInt())
        byteBuf.writeBlockVector3(this.position)
        byteBuf.writeActorUniqueID(this.targetActorID)
    }

    override fun pid(): Int {
        return ProtocolInfo.CONTAINER_OPEN_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
