package cn.nukkit.item.enchantment.damage

import cn.nukkit.entity.*
import cn.nukkit.item.enchantment.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EnchantmentDamageSmite :
    EnchantmentDamage(Enchantment.Companion.ID_DAMAGE_SMITE, "undead", Rarity.UNCOMMON, TYPE.SMITE) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 5 + (level - 1) * 8
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return this.getMinEnchantAbility(level) + 20
    }

    override fun getDamageBonus(target: Entity, damager: Entity): Double {
        if (target is EntitySmite) {
            return getLevel() * 2.5
        }

        return 0.0
    }
}
