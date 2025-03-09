package org.chorus.event.inventory

import org.chorus.entity.projectile.abstract_arrow.EntityArrow
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.inventory.Inventory

/**
 * @author MagicDroidX (Nukkit Project)
 */
class InventoryPickupArrowEvent(inventory: Inventory, val arrow: EntityArrow) : InventoryEvent(inventory),
    Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
