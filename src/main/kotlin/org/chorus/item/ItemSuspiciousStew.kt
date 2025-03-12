package org.chorus.item

import org.chorus.Player
import org.chorus.entity.effect.EffectType

class ItemSuspiciousStew @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemFood(ItemID.Companion.SUSPICIOUS_STEW, meta, count, "Suspicious Stew") {
    override val maxStackSize: Int
        get() = 1

    override val foodRestore: Int
        get() = 6

    override val saturationRestore: Float
        get() = 7.2f

    override val isRequiresHunger: Boolean
        get() = false

    override fun onEaten(player: Player): Boolean {
        val effect = when (meta) {
            0 -> get(EffectType.NIGHT_VISION).setDuration(4 * 20)
            1 -> get(EffectType.JUMP_BOOST).setDuration(4 * 20)
            2 -> get(EffectType.WEAKNESS).setDuration(7 * 20)
            3 -> get(EffectType.BLINDNESS).setDuration(6 * 20)
            4 -> get(EffectType.POISON).setDuration(10 * 20)
            5 -> get(EffectType.SATURATION).setDuration(6 * 20)
            6 -> get(EffectType.FIRE_RESISTANCE).setDuration(2 * 20)
            7 -> get(EffectType.REGENERATION).setDuration(6 * 20)
            8 -> get(EffectType.WITHER).setDuration(6 * 20)
            else -> null
        }

        if (effect != null) {
            player.addEffect(effect)
        }

        return true
    }
}
