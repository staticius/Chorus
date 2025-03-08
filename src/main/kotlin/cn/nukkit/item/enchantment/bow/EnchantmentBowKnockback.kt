package cn.nukkit.item.enchantment.bow

import cn.nukkit.item.enchantment.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EnchantmentBowKnockback :
    EnchantmentBow(Enchantment.Companion.ID_BOW_KNOCKBACK, "arrowKnockback", Rarity.RARE) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 12 + (level - 1) * 20
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return this.getMinEnchantAbility(level) + 25
    }

    override val maxLevel: Int
        get() = 2
}
