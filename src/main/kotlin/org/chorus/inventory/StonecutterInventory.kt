package org.chorus.inventory

import cn.nukkit.Player
import cn.nukkit.block.BlockStonecutterBlock
import cn.nukkit.item.Item
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType
import cn.nukkit.recipe.Input

class StonecutterInventory(blockStonecutterBlock: BlockStonecutterBlock?) :
    ContainerInventory(blockStonecutterBlock, InventoryType.STONECUTTER, 3), CraftTypeInventory, InputInventory {
    override var holder: InventoryHolder?
        get() = super.getHolder() as BlockStonecutterBlock
        set(holder) {
            super.holder = holder
        }

    override fun onClose(who: Player) {
        super.onClose(who)
        val drops = who.inventory.addItem(this.getItem(0))
        for (drop in drops) {
            if (!who.dropItem(drop)) {
                holder.level.dropItem(holder.vector3.add(0.5, 0.5, 0.5), drop)
            }
        }
        this.clear(0)
    }

    override fun setItem(index: Int, item: Item, send: Boolean): Boolean {
        if (index == 3) {
            slots[index] = item
            return true
        } else return false
    }

    override val input: Input
        get() = Input(
            1,
            1,
            arrayOf(arrayOf(getItem(3)))
        )

    override fun slotTypeMap(): MutableMap<Int?, ContainerSlotType?> {
        val map = super.slotTypeMap()
        map!![3] = ContainerSlotType.STONECUTTER_INPUT
        map[4] = ContainerSlotType.STONECUTTER_RESULT
        return map
    }
}
