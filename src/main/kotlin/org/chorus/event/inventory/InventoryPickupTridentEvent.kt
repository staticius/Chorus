package org.chorus.event.inventory

import cn.nukkit.entity.projectile.abstract_arrow.EntityThrownTrident
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.inventory.Inventory

class InventoryPickupTridentEvent(inventory: Inventory, val trident: EntityThrownTrident) :
    InventoryEvent(inventory), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
