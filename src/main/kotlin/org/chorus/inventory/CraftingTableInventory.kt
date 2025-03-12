package org.chorus.inventory

import org.chorus.Player
import org.chorus.block.BlockCraftingTable
import org.chorus.item.Item
import org.chorus.network.protocol.ContainerOpenPacket
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus.recipe.Input
import java.util.List

class CraftingTableInventory(table: BlockCraftingTable?) : BaseInventory(table, InventoryType.WORKBENCH, 9),
    CraftTypeInventory, InputInventory {
    override fun init() {
        val map = super.networkSlotMap()
        for (i in 0..<getSize()) {
            map[i] = 32 + i
        }

        val map2 = super.slotTypeMap()
        for (i in 0..<getSize()) {
            map2[i] = ContainerSlotType.CRAFTING_INPUT
            map2[i + 32] = ContainerSlotType.CRAFTING_INPUT
        }
    }

    override fun onOpen(who: Player) {
        super.onOpen(who)
        val pk = ContainerOpenPacket()
        pk.windowId = who.getWindowId(this)
        pk.type = getType().networkType
        val holder = this.holder
        pk.x = holder.x.toInt()
        pk.y = holder.y.toInt()
        pk.z = holder.z.toInt()
        who.dataPacket(pk)
        this.sendContents(who)
    }

    override var holder: InventoryHolder?
        get() = super.getHolder() as BlockCraftingTable
        set(holder) {
            super.holder = holder
        }

    override val input: Input
        get() {
            val item1 =
                List.of(getItem(0), getItem(1), getItem(2))
                    .toArray<Item>(Item.EMPTY_ARRAY)
            val item2 =
                List.of(getItem(3), getItem(4), getItem(5))
                    .toArray<Item>(Item.EMPTY_ARRAY)
            val item3 =
                List.of(getItem(6), getItem(7), getItem(8))
                    .toArray<Item>(Item.EMPTY_ARRAY)
            val items =
                arrayOf(item1, item2, item3)
            return Input(3, 3, items)
        }
}
