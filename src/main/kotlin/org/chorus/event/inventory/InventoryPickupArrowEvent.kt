package org.chorus.event.inventory

import cn.nukkit.entity.projectile.abstract_arrow.EntityArrow
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.inventory.Inventory

/**
 * @author MagicDroidX (Nukkit Project)
 */
class InventoryPickupArrowEvent(inventory: Inventory, val arrow: EntityArrow) : InventoryEvent(inventory),
    Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
