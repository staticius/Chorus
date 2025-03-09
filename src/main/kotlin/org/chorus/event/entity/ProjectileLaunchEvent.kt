package org.chorus.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class ProjectileLaunchEvent(entity: EntityProjectile?, var shooter: Entity) : EntityEvent(), Cancellable {
    init {
        this.entity = entity
    }

    override var entity: Entity?
        get() = entity as EntityProjectile
        set(entity) {
            super.entity = entity
        }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
