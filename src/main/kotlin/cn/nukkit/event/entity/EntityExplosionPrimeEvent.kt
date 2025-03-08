package cn.nukkit.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

/**
 * @since 15-10-27
 */
class EntityExplosionPrimeEvent(entity: Entity?, force: Double) : EntityEvent(), Cancellable {
    var force: Double
    var isBlockBreaking: Boolean
    var fireChance: Double = 0.0

    init {
        this.entity = entity
        this.force = force
        this.isBlockBreaking = true
    }

    var isIncendiary: Boolean
        get() = fireChance > 0
        set(incendiary) {
            if (!incendiary) {
                fireChance = 0.0
            } else if (fireChance <= 0) {
                fireChance = 1.0 / 3.0
            }
        }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
