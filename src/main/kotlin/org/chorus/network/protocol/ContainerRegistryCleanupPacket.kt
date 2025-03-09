package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.inventory.FullContainerName
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.*
import java.util.function.Consumer

@Getter
@Setter
@ToString
@AllArgsConstructor
class ContainerRegistryCleanupPacket : DataPacket() {
    private val removedContainers: List<FullContainerName> = ObjectArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray<FullContainerName>(
            this.getRemovedContainers(),
            Consumer<FullContainerName> { fullContainerName: FullContainerName? ->
                byteBuf.writeFullContainerName(
                    fullContainerName!!
                )
            })
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CONTAINER_REGISTRY_CLEANUP_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
