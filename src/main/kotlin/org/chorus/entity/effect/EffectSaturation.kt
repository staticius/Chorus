package org.chorus.entity.effect

import org.chorus.Player
import org.chorus.entity.Entity
import java.awt.Color

class EffectSaturation :
    InstantEffect(EffectType.SATURATION, "%potion.saturation", Color(248, 36, 35)) {
    override fun apply(entity: Entity, tickCount: Double) {
        if (entity is Player) {
            entity.foodData.addFood(this.getLevel(), (2 * this.getLevel()).toFloat())
        }
    }
}
