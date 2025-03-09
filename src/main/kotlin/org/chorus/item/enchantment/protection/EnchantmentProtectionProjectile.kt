package org.chorus.item.enchantment.protection

import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.item.enchantment.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EnchantmentProtectionProjectile : EnchantmentProtection(
    Enchantment.Companion.ID_PROTECTION_PROJECTILE,
    "projectile",
    Rarity.UNCOMMON,
    TYPE.PROJECTILE
) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 3 + (level - 1) * 6
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return this.getMinEnchantAbility(level) + 6
    }

    override val typeModifier: Double
        get() = 3.0

    override fun getProtectionFactor(e: EntityDamageEvent): Float {
        val cause = e.cause

        if (level <= 0 || (cause != DamageCause.PROJECTILE)) {
            return 0f
        }

        return (getLevel() * typeModifier).toFloat()
    }
}
