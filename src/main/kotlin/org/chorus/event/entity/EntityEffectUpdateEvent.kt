package org.chorus.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.entity.effect.Effect
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class EntityEffectUpdateEvent(entity: Entity?, oldEffect: Effect, newEffect: Effect) :
    EntityEvent(), Cancellable {
    val oldEffect: Effect
    val newEffect: Effect

    init {
        this.entity = entity
        this.oldEffect = oldEffect
        this.newEffect = newEffect
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
