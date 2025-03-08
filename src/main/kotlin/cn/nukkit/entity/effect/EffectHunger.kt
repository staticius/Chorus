package cn.nukkit.entity.effect

import cn.nukkit.Player
import cn.nukkit.entity.*
import java.awt.Color

class EffectHunger :
    Effect(EffectType.Companion.HUNGER, "%potion.hunger", Color(88, 118, 83)) {
    override fun canTick(): Boolean {
        return true
    }

    override fun apply(entity: Entity, tickCount: Double) {
        if (entity is Player) {
            entity.foodData.exhaust(0.1 * this.getLevel())
        }
    }
}
