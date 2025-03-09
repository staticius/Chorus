package org.chorus.item.enchantment.trident

import cn.nukkit.item.enchantment.*

class EnchantmentTridentRiptide :
    EnchantmentTrident(Enchantment.Companion.ID_TRIDENT_RIPTIDE, "tridentRiptide", Rarity.RARE) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 7 * level + 10
    }

    override val maxLevel: Int
        get() = 3

    public override fun checkCompatibility(enchantment: Enchantment): Boolean {
        return super.checkCompatibility(enchantment)
                && enchantment.id != Enchantment.Companion.ID_TRIDENT_LOYALTY && enchantment.id != Enchantment.Companion.ID_TRIDENT_CHANNELING
    }
}
