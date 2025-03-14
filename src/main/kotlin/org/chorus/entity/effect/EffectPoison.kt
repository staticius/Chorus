package org.chorus.entity.effect

import org.chorus.entity.Entity
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import java.awt.Color

class EffectPoison :
    Effect(EffectType.POISON, "%potion.poison", Color(135, 163, 99), true) {
    override fun canTick(): Boolean {
        val interval: Int = 25 shr this.getAmplifier()
        return interval > 0 && this.getDuration() % interval == 0
    }

    override fun apply(entity: Entity, tickCount: Double) {
        if (this.canTick()) {
            if (entity.getHealth() > 1) {
                entity.attack(EntityDamageEvent(entity, DamageCause.MAGIC, 1f))
            }
        }
    }
}
