package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.entity.item.EntityItem
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class ItemDespawnEvent(item: EntityItem) : EntityEvent(), Cancellable {
    override var entity: Entity = item

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
