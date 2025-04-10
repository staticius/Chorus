package org.chorus.event.inventory

import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.inventory.Inventory
import org.chorus.inventory.InventoryHolder
import org.chorus.item.Item

/**
 * Called when inventory transaction is not caused by a player
 */
class InventoryMoveItemEvent(
    from: Inventory,
    val targetInventory: Inventory,
    val source: InventoryHolder,
    var item: Item,
    val action: Action
) :
    InventoryEvent(from), Cancellable {
    enum class Action {
        SLOT_CHANGE,  //transaction between 2 inventories
        PICKUP,
        DROP,
        DISPENSE
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
