package org.chorus.entity.effect

import org.chorus.entity.Entity
import org.chorus.entity.EntityLiving
import java.awt.Color

class EffectSpeed :
    Effect(EffectType.SPEED, "%potion.moveSpeed", Color(51, 235, 255)) {
    override fun add(entity: Entity) {
        if (entity is EntityLiving) {
            val oldEffect: Effect? = entity.getEffect(this.getType())
            if (oldEffect != null) {
                entity.setMovementSpeed(entity.getMovementSpeed() / (1 + 0.2f * oldEffect.getLevel()))
            }

            entity.setMovementSpeed(entity.getMovementSpeed() * (1 + 0.2f * this.getLevel()))
        }
    }

    override fun remove(entity: Entity) {
        if (entity is EntityLiving) {
            entity.setMovementSpeed(entity.getMovementSpeed() / (1 + 0.2f * this.getLevel()))
        }
    }
}
