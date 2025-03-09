package org.chorus.event.inventory

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.inventory.EnchantInventory
import org.chorus.item.Item

class EnchantItemEvent(
    inventory: EnchantInventory,
    var oldItem: Item,
    var newItem: Item,
    var xpCost: Int,
    var enchanter: Player
) :
    InventoryEvent(inventory), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
