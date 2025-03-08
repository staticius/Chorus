package cn.nukkit.item.enchantment.trident

import cn.nukkit.item.enchantment.*

class EnchantmentTridentLoyalty :
    EnchantmentTrident(Enchantment.Companion.ID_TRIDENT_LOYALTY, "tridentLoyalty", Rarity.UNCOMMON) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 7 * level + 5
    }

    override val maxLevel: Int
        get() = 3
}
