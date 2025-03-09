package org.chorus.event.inventory

import cn.nukkit.blockentity.BlockEntityBrewingStand
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

/**
 * @author CreeperFace
 */
class BrewEvent(val brewingStand: BlockEntityBrewingStand) : InventoryEvent(brewingStand.inventory), Cancellable {
    val ingredient: Item = brewingStand.inventory.ingredient

    val potions: Array<Item?> = arrayOfNulls(3)

    val fuel: Int = brewingStand.fuel

    init {
        for (i in 0..2) {
            potions[i] = brewingStand.inventory.getItem(i)
        }
    }

    /**
     * @param index Potion index in range 0 - 2
     * @return potion
     */
    fun getPotion(index: Int): Item? {
        return potions[index]
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
