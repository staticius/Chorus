package org.chorus.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.entity.item.EntityItem
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

/**
 * @author MagicDroidX (Nukkit Project)
 */
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
