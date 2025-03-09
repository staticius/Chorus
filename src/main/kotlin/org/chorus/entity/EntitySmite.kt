package org.chorus.entity

import org.chorus.entity.effect.*
import org.chorus.inventory.EntityInventoryHolder
import org.chorus.level.*

/**
 * 这个接口代表亡灵类的怪物实体
 *
 *
 * This interface represents the monster entity of the undead class
 *
 * @author MagicDroidX (Nukkit Project)
 */
interface EntitySmite {
    fun burn(entity: Entity) {
        if (entity.level!!.getDimension() == Level.DIMENSION_OVERWORLD && entity.level!!.isDaytime() && !entity.level!!.isRaining() && (!entity.hasEffect(
                EffectType.Companion.FIRE_RESISTANCE
            ) || (entity is EntityInventoryHolder && entity.getHelmet().isNull()))
            && !entity.isInsideOfWater() && !entity.isUnderBlock() && !entity.isOnFire()
        ) {
            entity.setOnFire(1)
        }
    }
}
