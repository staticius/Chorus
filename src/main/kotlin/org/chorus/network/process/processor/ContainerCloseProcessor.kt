package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.inventory.SpecialWindowId
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ContainerClosePacket
import org.chorus.network.protocol.ProtocolInfo

class ContainerCloseProcessor : DataPacketProcessor<ContainerClosePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: ContainerClosePacket) {
        val player = playerHandle.player
        if (!player.spawned || pk.windowId == SpecialWindowId.PLAYER.id && !playerHandle.inventoryOpen) {
            return
        }

        val inventory = player.getWindowById(pk.windowId)

        if (playerHandle.windowIndex.containsKey(pk.windowId)) {
            if (pk.windowId == SpecialWindowId.PLAYER.id) {
                playerHandle.closingWindowId = pk.windowId
                player.getInventory().close(player)
                playerHandle.inventoryOpen = false
            } else {
                playerHandle.removeWindow(playerHandle.windowIndex[pk.windowId]!!)
            }
        }

        if (pk.windowId == -1) {
            player.addWindow(player.craftingGrid!!, SpecialWindowId.NONE.id)
        }
        if (inventory != null) {
            val pk2 = ContainerClosePacket()
            pk2.wasServerInitiated = false
            pk2.windowId = pk.windowId
            pk2.type = inventory.type
            player.dataPacket(pk2)
            player.resetInventory()
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.CONTAINER_CLOSE_PACKET
}
