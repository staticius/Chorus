package org.chorus.item.enchantment.crossbow

import org.chorus.item.enchantment.*

class EnchantmentCrossbowPiercing :
    EnchantmentCrossbow(Enchantment.Companion.ID_CROSSBOW_PIERCING, "crossbowPiercing", Rarity.COMMON) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 1 + 10 * (level - 1)
    }

    override val maxLevel: Int
        get() = 4

    public override fun checkCompatibility(enchantment: Enchantment): Boolean {
        return super.checkCompatibility(enchantment) && enchantment.id != Enchantment.Companion.ID_CROSSBOW_MULTISHOT
    }
}
