package cn.nukkit.item.enchantment.bow

import cn.nukkit.item.enchantment.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EnchantmentBowFlame :
    EnchantmentBow(Enchantment.Companion.ID_BOW_FLAME, "arrowFire", Rarity.RARE) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 20
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return 50
    }
}
