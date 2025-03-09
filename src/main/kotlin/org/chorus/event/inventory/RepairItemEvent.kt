package org.chorus.event.inventory

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.inventory.AnvilInventory
import org.chorus.item.Item

class RepairItemEvent(
    inventory: AnvilInventory,
    val oldItem: Item,
    val newItem: Item,
    val materialItem: Item,
    var cost: Int,
    val player: Player
) :
    InventoryEvent(inventory), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
