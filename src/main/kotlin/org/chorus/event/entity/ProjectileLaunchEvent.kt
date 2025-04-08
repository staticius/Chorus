package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class ProjectileLaunchEvent(entity: EntityProjectile, var shooter: Entity) : EntityEvent(), Cancellable {
    override var entity: Entity = entity

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
