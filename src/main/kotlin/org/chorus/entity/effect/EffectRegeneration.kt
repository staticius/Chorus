package org.chorus.entity.effect

import org.chorus.entity.*
import org.chorus.event.entity.EntityRegainHealthEvent
import java.awt.Color
import kotlin.math.min

class EffectRegeneration : Effect(EffectType.REGENERATION, "%potion.regeneration", Color(205, 92, 171)) {
    override fun canTick(): Boolean {
        val amplifier: Int = min(5.0, getAmplifier().toDouble()).toInt()
        val interval: Int = 50 shr amplifier
        return interval > 0 && this.getDuration() % interval == 0
    }

    override fun apply(entity: Entity, tickCount: Double) {
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(EntityRegainHealthEvent(entity, 1f, EntityRegainHealthEvent.CAUSE_MAGIC))
        }
    }
}
