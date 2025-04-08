
package org.chorus.event.inventory

import org.chorus.Player
import org.chorus.event.HandlerList
import org.chorus.inventory.AnvilInventory
import org.chorus.inventory.Inventory

class PlayerTypingAnvilInventoryEvent(
    val player: Player,
    inventory: AnvilInventory,
    val previousName: String?,
    val typedName: String
) : InventoryEvent(inventory) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
