package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EntityArmorChangeEvent(entity: Entity?, oldItem: Item, newItem: Item, slot: Int) :
    EntityEvent(), Cancellable {
    val oldItem: Item
    @JvmField
    var newItem: Item
    val slot: Int

    init {
        this.entity = entity
        this.oldItem = oldItem
        this.newItem = newItem
        this.slot = slot
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
