package org.chorus.item.enchantment.trident

import org.chorus.item.enchantment.Enchantment

class EnchantmentTridentLoyalty :
    EnchantmentTrident(ID_TRIDENT_LOYALTY, "tridentLoyalty", Rarity.UNCOMMON) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 7 * level + 5
    }

    override val maxLevel: Int
        get() = 3
}
