package org.chorus.item.enchantment.damage

import org.chorus.item.Item
import org.chorus.item.enchantment.Enchantment
import org.chorus.item.enchantment.EnchantmentType


abstract class EnchantmentDamage protected constructor(
    id: Int,
    name: String,
    rarity: Rarity,
    protected val damageType: TYPE
) :
    Enchantment(id, name, rarity, EnchantmentType.SWORD) {
    enum class TYPE {
        ALL,
        SMITE,
        ARTHROPODS
    }

    public override fun checkCompatibility(enchantment: Enchantment): Boolean {
        return enchantment !is EnchantmentDamage
    }

    override fun canEnchant(item: Item): Boolean {
        return item.isAxe || super.canEnchant(item)
    }

    override val maxLevel: Int
        get() = 5

    override fun getName(): String {
        return "%enchantment.damage." + this.name
    }

    override val isMajor: Boolean
        get() = true
}
