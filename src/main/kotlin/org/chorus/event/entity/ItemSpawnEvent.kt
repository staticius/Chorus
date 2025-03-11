package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.entity.item.EntityItem
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList


class ItemSpawnEvent(item: EntityItem?) : EntityEvent(), Cancellable {
    init {
        this.entity = item
    }

    override var entity: Entity?
        get() = entity as EntityItem
        set(entity) {
            super.entity = entity
        }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
