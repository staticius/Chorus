package cn.nukkit.item.enchantment.trident

import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.item.enchantment.EnchantmentType

abstract class EnchantmentTrident protected constructor(id: Int, name: String, rarity: Rarity) :
    Enchantment(id, name, rarity, EnchantmentType.TRIDENT) {
    override fun getMaxEnchantAbility(level: Int): Int {
        return 50
    }
}
