package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.inventory.InventoryType
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.utils.Loggable

data class ContainerClosePacket(
    val containerID: Int,
    val containerType: InventoryType,
    val serverInitiatedClose: Boolean,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(containerID)
        byteBuf.writeByte(containerType.networkType)
        byteBuf.writeBoolean(this.serverInitiatedClose)
    }

    override fun pid(): Int {
        return ProtocolInfo.CONTAINER_CLOSE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<ContainerClosePacket>, Loggable {
        override fun decode(byteBuf: HandleByteBuf): ContainerClosePacket {
            return ContainerClosePacket(
                containerID = byteBuf.readByte().toInt(),
                containerType = InventoryType.from(byteBuf.readByte().toInt()),
                serverInitiatedClose = byteBuf.readBoolean()
            )
        }
    }
}
