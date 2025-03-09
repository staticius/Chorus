package org.chorus.entity.effect

import cn.nukkit.entity.*
import java.awt.Color

class EffectHealthBoost : Effect(EffectType.Companion.HEALTH_BOOST, "%potion.healthBoost", Color(248, 125, 35)) {
    override fun add(entity: Entity) {
        entity.setMaxHealth(entity.getMaxHealth() + 4 * this.getLevel())
    }

    override fun remove(entity: Entity) {
        entity.setMaxHealth(entity.getMaxHealth() - 4 * this.getLevel())
        if (entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth().toFloat())
        }
    }
}
