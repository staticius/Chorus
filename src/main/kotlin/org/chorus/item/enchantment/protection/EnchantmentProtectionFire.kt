package org.chorus.item.enchantment.protection

import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.item.enchantment.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EnchantmentProtectionFire :
    EnchantmentProtection(Enchantment.Companion.ID_PROTECTION_FIRE, "fire", Rarity.UNCOMMON, TYPE.FIRE) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 10 + (level - 1) * 8
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return this.getMinEnchantAbility(level) + 8
    }

    override val typeModifier: Double
        get() = 2.0

    override fun getProtectionFactor(e: EntityDamageEvent): Float {
        val cause = e.cause

        if (level <= 0 || (cause != DamageCause.LAVA && cause != DamageCause.FIRE && cause != DamageCause.FIRE_TICK)) {
            return 0f
        }

        return (getLevel() * typeModifier).toFloat()
    }
}
