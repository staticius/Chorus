package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.level.Level

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EntityLevelChangeEvent(entity: Entity?, originLevel: Level, targetLevel: Level) :
    EntityEvent(), Cancellable {
    val origin: Level
    val target: Level

    init {
        this.entity = entity
        this.origin = originLevel
        this.target = targetLevel
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
