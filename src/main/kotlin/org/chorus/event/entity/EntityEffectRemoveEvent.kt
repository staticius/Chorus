package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.entity.effect.Effect
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class EntityEffectRemoveEvent(entity: Entity, effect: Effect) : EntityEvent(),
    Cancellable {
    val removeEffect: Effect

    init {
        this.entity = entity
        this.removeEffect = effect
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
