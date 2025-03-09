package org.chorus.inventory

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType
import cn.nukkit.recipe.Input
import java.util.List

class CraftingGridInventory(holder: Player?) : BaseInventory(holder, InventoryType.INVENTORY, 4),
    InputInventory {
    override fun init() {
        val map2 = super.slotTypeMap()
        for (i in 0..<getSize()) {
            map2!![i] = ContainerSlotType.CRAFTING_INPUT
        }

        val map1 = super.networkSlotMap()
        for (i in 0..<getSize()) {
            map1!![i] = 28 + i
        }
    }

    override var holder: InventoryHolder?
        get() = super.getHolder() as Player
        set(holder) {
            super.holder = holder
        }

    override val input: Input
        get() {
            val item1 =
                List.of(getItem(0), getItem(1))
                    .toArray<Item>(Item.EMPTY_ARRAY)
            val item2 =
                List.of(getItem(2), getItem(3))
                    .toArray<Item>(Item.EMPTY_ARRAY)
            val items =
                arrayOf(item1, item2)
            return Input(2, 2, items)
        }
}
