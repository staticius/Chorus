package org.chorus.item.enchantment.crossbow

import org.chorus.item.enchantment.*

class EnchantmentCrossbowMultishot :
    EnchantmentCrossbow(Enchantment.Companion.ID_CROSSBOW_MULTISHOT, "crossbowMultishot", Rarity.RARE) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 20
    }

    override val maxLevel: Int
        get() = 1

    public override fun checkCompatibility(enchantment: Enchantment): Boolean {
        return super.checkCompatibility(enchantment) && enchantment.id != Enchantment.Companion.ID_CROSSBOW_PIERCING
    }
}
