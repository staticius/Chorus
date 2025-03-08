package cn.nukkit.item.enchantment.bow

import cn.nukkit.item.enchantment.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EnchantmentBowInfinity :
    EnchantmentBow(Enchantment.Companion.ID_BOW_INFINITY, "arrowInfinite", Rarity.VERY_RARE) {
    override fun checkCompatibility(enchantment: Enchantment): Boolean {
        return super.checkCompatibility(enchantment) && enchantment.id != Enchantment.Companion.ID_MENDING
    }

    override fun getMinEnchantAbility(level: Int): Int {
        return 20
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return 50
    }
}
