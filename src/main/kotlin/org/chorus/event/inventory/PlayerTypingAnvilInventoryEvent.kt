
package org.chorus.event.inventory

import org.chorus.Player
import org.chorus.event.HandlerList
import org.chorus.inventory.AnvilInventory
import org.chorus.inventory.Inventory


/**
 * Fired when a player change anything in the item name in an open Anvil inventory window.
 *
 * @author joserobjr
 * @since 2021-02-14
 */

class PlayerTypingAnvilInventoryEvent(
    val player: Player,
    inventory: AnvilInventory,
    val previousName: String?,
    val typedName: String
) :
    InventoryEvent(inventory) {
    override val inventory: Inventory
        get() = super.getInventory() as AnvilInventory

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
