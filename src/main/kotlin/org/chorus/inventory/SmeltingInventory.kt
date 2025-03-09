package org.chorus.inventory

import org.chorus.blockentity.BlockEntity.scheduleUpdate
import org.chorus.blockentity.BlockEntityFurnace
import org.chorus.item.*

abstract class SmeltingInventory(holder: InventoryHolder?, type: InventoryType, size: Int) :
    ContainerInventory(holder, type, size) {
    val result: Item
        get() = this.getItem(2)

    val fuel: Item
        get() = this.getItem(1)

    val smelting: Item
        get() = this.getItem(0)

    fun setResult(item: Item?): Boolean {
        return this.setItem(2, item!!)
    }

    fun setFuel(item: Item?): Boolean {
        return this.setItem(1, item!!)
    }

    fun setSmelting(item: Item?): Boolean {
        return this.setItem(0, item!!)
    }

    override var holder: InventoryHolder?
        get() = holder as BlockEntityFurnace
        set(holder) {
            super.holder = holder
        }

    override fun onSlotChange(index: Int, before: Item, send: Boolean) {
        super.onSlotChange(index, before, send)
        if (index == 2 && (before.isNull || before.getCount() > 0)) {
            val holder: BlockEntityFurnace? = holder
            val xp = holder!!.calculateXpDrop()
            if (xp > 0) {
                holder.storedXP = 0f
                holder.level.dropExpOrb(holder.position, xp.toInt())
            }
        }
        holder.scheduleUpdate()
    }
}
