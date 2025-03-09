package org.chorus.item.enchantment

import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageModifier

class EnchantmentBreach : Enchantment(Enchantment.Companion.ID_BREACH, "breach", Rarity.RARE, EnchantmentType.MACE) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 10 + (level - 1) * 20
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return super.getMinEnchantAbility(level) + 50
    }

    override val maxLevel: Int
        get() = 4

    override fun checkCompatibility(enchantment: Enchantment): Boolean {
        val id = enchantment.getId()
        return id != Enchantment.Companion.ID_DAMAGE_ALL && id != Enchantment.Companion.ID_DENSITY && id != Enchantment.Companion.ID_DAMAGE_SMITE && id != Enchantment.Companion.ID_DAMAGE_ARTHROPODS
    }

    val armorEfficiencyReduction: Float
        get() = (100 - (getLevel() * 15)) / 100f

    override fun doAttack(event: EntityDamageByEntityEvent) {
        val reduction = armorEfficiencyReduction
        event.setDamage(event.getDamage(DamageModifier.ARMOR) * reduction, DamageModifier.ARMOR)
        event.setDamage(
            event.getDamage(DamageModifier.ARMOR_ENCHANTMENTS) * reduction,
            DamageModifier.ARMOR_ENCHANTMENTS
        )
    }
}
