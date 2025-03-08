package cn.nukkit.entity.effect

import cn.nukkit.entity.*
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import java.awt.Color

class EffectFatalPoison :
    Effect(EffectType.Companion.FATAL_POISON, "%potion.poison", Color(78, 147, 49), true) {
    override fun canTick(): Boolean {
        val interval: Int = 25 shr this.getAmplifier()
        return interval > 0 && this.getDuration() % interval == 0
    }

    override fun apply(entity: Entity, tickCount: Double) {
        if (this.canTick()) {
            entity.attack(EntityDamageEvent(entity, DamageCause.MAGIC, 1f))
        }
    }
}
