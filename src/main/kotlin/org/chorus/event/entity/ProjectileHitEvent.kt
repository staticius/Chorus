package org.chorus.event.entity

import org.chorus.entity.projectile.EntityProjectile
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.level.MovingObjectPosition

class ProjectileHitEvent @JvmOverloads constructor(
    entity: EntityProjectile,
    movingObjectPosition: MovingObjectPosition? = null
) :
    EntityEvent(), Cancellable {
    var movingObjectPosition: MovingObjectPosition?

    init {
        this.entity = entity
        this.movingObjectPosition = movingObjectPosition
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
