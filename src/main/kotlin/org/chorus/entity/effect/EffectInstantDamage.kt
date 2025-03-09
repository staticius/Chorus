package org.chorus.entity.effect

import cn.nukkit.entity.*
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.event.entity.EntityRegainHealthEvent
import java.awt.Color

class EffectInstantDamage :
    InstantEffect(EffectType.Companion.INSTANT_DAMAGE, "%potion.harm", Color(169, 101, 106), true) {
    override fun apply(entity: Entity, tickCount: Double) {
        val amount: Double = (6 shl this.getAmplifier()) * tickCount
        if (entity.isUndead()) {
            entity.heal(EntityRegainHealthEvent(entity, amount.toFloat(), EntityRegainHealthEvent.CAUSE_MAGIC))
        } else {
            entity.attack(EntityDamageEvent(entity, DamageCause.MAGIC, amount.toFloat()))
        }
    }
}
