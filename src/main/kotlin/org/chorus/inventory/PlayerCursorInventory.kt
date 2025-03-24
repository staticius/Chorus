package org.chorus.inventory

import org.chorus.Player
import org.chorus.Server
import org.chorus.item.Item
import org.chorus.network.protocol.InventorySlotPacket
import org.chorus.network.protocol.types.inventory.FullContainerName
import org.chorus.network.protocol.types.itemstack.ContainerSlotType


class PlayerCursorInventory(player: Player?) : BaseInventory(player, InventoryType.INVENTORY, 1) {
    override var holder: InventoryHolder?
        /**
         * This override is here for documentation and code completion purposes only.
         *
         * @return Player
         */
        get() = super.getHolder() as Player
        set(holder) {
            super.holder = holder
        }

    val item: Item
        get() = getItem(0)

    override fun init() {
        val slotTypeMap = super.slotTypeMap()
        slotTypeMap[0] = ContainerSlotType.CURSOR
    }

    override fun sendSlot(index: Int, vararg players: Player) {
        val pk = InventorySlotPacket()
        pk.item = this.getUnclonedItem(index)
        pk.slot = index

        for (player in players) {
            val id = SpecialWindowId.CURSOR.id
            pk.inventoryId = id
            pk.fullContainerName = FullContainerName(
                ContainerSlotType.CURSOR,
                id
            )
            player.dataPacket(pk)
        }
    }

    override fun sendContents(vararg players: Player) {
        val inventorySlotPacket = InventorySlotPacket()
        val id = SpecialWindowId.CURSOR.id
        inventorySlotPacket.inventoryId = id
        inventorySlotPacket.item = getUnclonedItem(0)
        inventorySlotPacket.slot = 0
        inventorySlotPacket.fullContainerName = FullContainerName(
            ContainerSlotType.CURSOR,
            id
        )
        Server.broadcastPacket(players, inventorySlotPacket)
    }
}
