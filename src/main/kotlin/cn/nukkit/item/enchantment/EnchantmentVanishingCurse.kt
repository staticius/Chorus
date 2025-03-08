package cn.nukkit.item.enchantment

import cn.nukkit.block.BlockID
import cn.nukkit.item.*

class EnchantmentVanishingCurse : Enchantment(
    Enchantment.Companion.ID_VANISHING_CURSE,
    "curse.vanishing",
    Rarity.VERY_RARE,
    EnchantmentType.BREAKABLE
) {
    init {
        this.isObtainableFromEnchantingTable = false
    }

    override fun getMinEnchantAbility(level: Int): Int {
        return 25
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return 50
    }

    override fun canEnchant(item: Item): Boolean {
        return when (item.id) {
            BlockID.SKULL, ItemID.Companion.COMPASS -> true
            else -> {
                if (item.isBlock && item.block.id == BlockID.CARVED_PUMPKIN) {
                    true
                }
                super.canEnchant(item)
            }
        }
    }
}
