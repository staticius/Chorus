package org.chorus.event.inventory

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.inventory.Inventory

class InventoryOpenEvent(inventory: Inventory, val player: Player) : InventoryEvent(inventory),
    Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
