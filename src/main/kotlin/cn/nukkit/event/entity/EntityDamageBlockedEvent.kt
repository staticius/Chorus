package cn.nukkit.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause

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
