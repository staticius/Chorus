package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.inventory.FullContainerName

data class ContainerRegistryCleanupPacket(
    val removedContainers: List<FullContainerName>
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(this.removedContainers) { fullContainerName ->
            byteBuf.writeFullContainerName(fullContainerName)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.CONTAINER_REGISTRY_CLEANUP_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
