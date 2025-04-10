package org.chorus.event.inventory

import org.chorus.Player
import org.chorus.event.HandlerList
import org.chorus.inventory.Inventory

class InventoryCloseEvent(inventory: Inventory, val player: Player) : InventoryEvent(inventory) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
