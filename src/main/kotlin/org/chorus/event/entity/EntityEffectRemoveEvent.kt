package org.chorus.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.entity.effect.Effect
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class EntityEffectRemoveEvent(entity: Entity?, effect: Effect) : EntityEvent(),
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
