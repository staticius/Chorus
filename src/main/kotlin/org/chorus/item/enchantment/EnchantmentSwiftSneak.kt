package org.chorus.item.enchantment

class EnchantmentSwiftSneak : Enchantment(
    Enchantment.Companion.ID_SWIFT_SNEAK,
    Enchantment.Companion.NAME_SWIFT_SNEAK,
    Rarity.VERY_RARE,
    EnchantmentType.ARMOR_LEGS
) {
    init {
        this.isObtainableFromEnchantingTable = false
    }

    override fun getMinEnchantAbility(level: Int): Int {
        return 10 * level
    }

    override val maxLevel: Int
        get() = 3
}
