package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.event.entity.EntityDamageEvent.DamageCause

class EntityDamageBlockedEvent(entity: Entity?, damage: EntityDamageEvent, knockBack: Boolean, animation: Boolean) :
    EntityEvent(), Cancellable {
    val damage: EntityDamageEvent
    val knockBackAttacker: Boolean
    val animation: Boolean

    init {
        this.entity = entity
        this.damage = damage
        this.knockBackAttacker = knockBack
        this.animation = animation
    }

    val cause: DamageCause?
        get() = damage.cause

    val attacker: Entity?
        get() = damage.getEntity()

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
