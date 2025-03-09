package org.chorus.event.inventory

import org.chorus.entity.item.EntityItem
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.inventory.Inventory

/**
 * @author MagicDroidX (Nukkit Project)
 */
class InventoryPickupItemEvent(inventory: Inventory, val item: EntityItem) : InventoryEvent(inventory),
    Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
