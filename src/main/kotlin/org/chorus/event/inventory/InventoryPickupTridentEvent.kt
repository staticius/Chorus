package org.chorus.event.inventory

import org.chorus.entity.projectile.abstract_arrow.EntityThrownTrident
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.inventory.Inventory

class InventoryPickupTridentEvent(inventory: Inventory, val trident: EntityThrownTrident) :
    InventoryEvent(inventory), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
