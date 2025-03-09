package org.chorus.item.enchantment.crossbow

import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.item.enchantment.EnchantmentType

abstract class EnchantmentCrossbow protected constructor(id: Int, name: String, rarity: Rarity) :
    Enchantment(id, name, rarity, EnchantmentType.CROSSBOW) {
    override fun getMaxEnchantAbility(level: Int): Int {
        return 50
    }
}
