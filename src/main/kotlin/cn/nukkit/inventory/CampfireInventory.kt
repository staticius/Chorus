package cn.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.blockentity.BlockEntity.scheduleUpdate
import cn.nukkit.blockentity.BlockEntityCampfire
import cn.nukkit.blockentity.BlockEntitySpawnable.spawnToAll
import cn.nukkit.item.*


class CampfireInventory(campfire: BlockEntityCampfire?) : ContainerInventory(campfire, InventoryType.NONE, 4) {
    override var holder: InventoryHolder?
        get() = holder as BlockEntityCampfire
        set(holder) {
            super.holder = holder
        }

    override fun onSlotChange(index: Int, before: Item, send: Boolean) {
        super.onSlotChange(index, before, send)
        holder.scheduleUpdate()
        holder.spawnToAll()
    }

    override var maxStackSize: Int
        get() = 1
        set(maxStackSize) {
            super.maxStackSize = maxStackSize
        }

    override fun open(who: Player): Boolean {
        return true
    }

    override fun close(who: Player) {
    }

    override fun onOpen(who: Player) {
    }

    override fun onClose(who: Player) {
    }
}
