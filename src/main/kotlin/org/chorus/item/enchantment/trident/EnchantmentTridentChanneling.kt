package org.chorus.item.enchantment.trident

import cn.nukkit.item.enchantment.*

class EnchantmentTridentChanneling :
    EnchantmentTrident(Enchantment.Companion.ID_TRIDENT_CHANNELING, "tridentChanneling", Rarity.VERY_RARE) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 25
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return 50
    }
}
