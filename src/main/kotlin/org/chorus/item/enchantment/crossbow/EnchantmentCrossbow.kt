package org.chorus.item.enchantment.crossbow

import org.chorus.item.enchantment.Enchantment
import org.chorus.item.enchantment.EnchantmentType

abstract class EnchantmentCrossbow protected constructor(id: Int, name: String, rarity: Rarity) :
    Enchantment(id, name, rarity, EnchantmentType.CROSSBOW) {
    override fun getMaxEnchantAbility(level: Int): Int {
        return 50
    }
}
