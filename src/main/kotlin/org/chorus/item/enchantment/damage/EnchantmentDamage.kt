package org.chorus.item.enchantment.damage

import cn.nukkit.item.Item
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.item.enchantment.EnchantmentType

/**
 * @author MagicDroidX (Nukkit Project)
 */
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
