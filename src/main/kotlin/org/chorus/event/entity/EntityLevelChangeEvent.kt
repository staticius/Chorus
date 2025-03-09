package org.chorus.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.level.Level

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
