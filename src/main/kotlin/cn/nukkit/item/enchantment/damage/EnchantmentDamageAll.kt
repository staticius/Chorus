package cn.nukkit.item.enchantment.damage

import cn.nukkit.entity.*
import cn.nukkit.item.enchantment.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EnchantmentDamageAll :
    EnchantmentDamage(Enchantment.Companion.ID_DAMAGE_ALL, "all", Rarity.COMMON, TYPE.ALL) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 1 + (level - 1) * 11
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return this.getMinEnchantAbility(level) + 20
    }

    override val maxLevel: Int
        get() = 5

    override fun getDamageBonus(target: Entity, damager: Entity): Double {
        val level = getLevel()
        if (level <= 0) {
            return 0.0
        }

        return level * 1.25
    }
}
