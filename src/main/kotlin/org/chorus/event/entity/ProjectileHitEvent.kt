package org.chorus.event.entity

import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.level.MovingObjectPosition

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ProjectileHitEvent @JvmOverloads constructor(
    entity: EntityProjectile?,
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
