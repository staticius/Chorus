package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.inventory.SpecialWindowId
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ContainerClosePacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo

class ContainerCloseProcessor : DataPacketProcessor<ContainerClosePacket>() {
    override fun handle(player: Player, pk: ContainerClosePacket) {
        val player = player.player
        if (!player.spawned || pk.containerID == SpecialWindowId.PLAYER.id && !player.player.inventoryOpen) {
            return
        }

        val inventory = player.getWindowById(pk.containerID)

        if (player.player.windowIndex.containsKey(pk.containerID)) {
            if (pk.containerID == SpecialWindowId.PLAYER.id) {
                player.player.closingWindowId = pk.containerID
                player.inventory.close(player)
                player.player.inventoryOpen = false
            } else {
                player.player.removeWindow(player.player.windowIndex[pk.containerID]!!)
            }
        }

        if (pk.containerID == -1) {
            player.addWindow(player.craftingGrid, SpecialWindowId.NONE.id)
        }
        if (inventory != null) {
            player.dataPacket(
                ContainerClosePacket(
                    containerID = pk.containerID,
                    containerType = inventory.type,
                    serverInitiatedClose = false,
                )
            )
            player.resetInventory()
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.CONTAINER_CLOSE_PACKET
}
