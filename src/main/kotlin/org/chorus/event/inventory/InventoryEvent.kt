package org.chorus.event.inventory

import org.chorus.Player
import org.chorus.event.Event
import org.chorus.inventory.Inventory


abstract class InventoryEvent(val inventory: Inventory) : Event() {
    val viewers: Array<Player>
        get() = inventory.viewers.toArray(Player.EMPTY_ARRAY)
}
