package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.entity.effect.Effect
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

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
