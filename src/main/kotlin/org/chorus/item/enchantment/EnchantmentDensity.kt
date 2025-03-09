package org.chorus.item.enchantment

import org.chorus.entity.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EnchantmentDensity : Enchantment(Enchantment.Companion.ID_DENSITY, "density", Rarity.RARE, EnchantmentType.MACE) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 10 + (level - 1) * 20
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return super.getMinEnchantAbility(level) + 50
    }

    override val maxLevel: Int
        get() = 5

    override fun checkCompatibility(enchantment: Enchantment): Boolean {
        val id = enchantment.getId()
        return id != Enchantment.Companion.ID_DAMAGE_ALL && id != Enchantment.Companion.ID_BREACH && id != Enchantment.Companion.ID_DAMAGE_SMITE && id != Enchantment.Companion.ID_DAMAGE_ARTHROPODS
    }

    override fun getDamageBonus(target: Entity, damager: Entity): Double {
        val height = damager.highestPosition - damager.position.y
        if (height >= 1.5f) {
            return height * 0.5f * getLevel()
        }
        return 0.0
    }
}
