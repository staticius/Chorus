package org.chorus.entity.effect

import cn.nukkit.entity.*
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import java.awt.Color

class EffectWither :
    Effect(EffectType.Companion.WITHER, "%potion.wither", Color(115, 97, 86), true) {
    override fun canTick(): Boolean {
        val interval: Int = 25 shr this.getAmplifier()
        return interval > 0 && this.getDuration() % interval == 0
    }

    override fun apply(entity: Entity, tickCount: Double) {
        entity.attack(EntityDamageEvent(entity, DamageCause.MAGIC, 1f))
    }
}
