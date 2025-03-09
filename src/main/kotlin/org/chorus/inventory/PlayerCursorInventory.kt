package org.chorus.inventory

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.item.Item
import cn.nukkit.network.protocol.InventorySlotPacket
import cn.nukkit.network.protocol.types.inventory.FullContainerName
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType

/**
 * @author CreeperFace
 */
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
        slotTypeMap!![0] = ContainerSlotType.CURSOR
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
