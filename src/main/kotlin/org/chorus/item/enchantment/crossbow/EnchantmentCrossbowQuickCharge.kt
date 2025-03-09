package org.chorus.item.enchantment.crossbow

import cn.nukkit.item.enchantment.*

class EnchantmentCrossbowQuickCharge :
    EnchantmentCrossbow(Enchantment.Companion.ID_CROSSBOW_QUICK_CHARGE, "crossbowQuickCharge", Rarity.UNCOMMON) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 12 + 20 * (level - 1)
    }

    override val maxLevel: Int
        get() = 3
}
