package org.chorus.item.enchantment.damage

import org.chorus.entity.*
import org.chorus.entity.effect.Effect.Companion.get
import org.chorus.entity.effect.EffectType
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.item.enchantment.*
import java.util.concurrent.ThreadLocalRandom


class EnchantmentDamageArthropods :
    EnchantmentDamage(Enchantment.Companion.ID_DAMAGE_ARTHROPODS, "arthropods", Rarity.UNCOMMON, TYPE.SMITE) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 5 + (level - 1) * 8
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return this.getMinEnchantAbility(level) + 20
    }

    override fun getDamageBonus(target: Entity, damager: Entity): Double {
        if (target is EntityArthropod) {
            return getLevel() * 2.5
        }

        return 0.0
    }

    override fun doAttack(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        if (entity is EntityArthropod) {
            val duration = 20 + ThreadLocalRandom.current().nextInt(10 * this.level)
            entity.addEffect(get(EffectType.SLOWNESS).setDuration(duration).setAmplifier(3))
        }
    }
}
