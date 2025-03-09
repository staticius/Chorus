package org.chorus.item.enchantment

import org.chorus.entity.*
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EnchantmentThorns :
    Enchantment(Enchantment.Companion.ID_THORNS, "thorns", Rarity.VERY_RARE, EnchantmentType.ARMOR) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 10 + (level - 1) * 20
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return super.getMinEnchantAbility(level) + 50
    }

    override val maxLevel: Int
        get() = 3

    override fun doPostAttack(attacker: Entity, entity: Entity) {
        if (entity !is EntityHumanType) {
            return
        }

        var thornsLevel = 0

        for (armor in entity.getInventory().armorContents) {
            val thorns = armor.getEnchantment(Enchantment.Companion.ID_THORNS)
            if (thorns != null) {
                thornsLevel = max(thorns.getLevel().toDouble(), thornsLevel.toDouble()).toInt()
            }
        }

        val random = ThreadLocalRandom.current()

        if (shouldHit(random, thornsLevel)) {
            attacker.attack(
                EntityDamageByEntityEvent(
                    entity,
                    attacker,
                    DamageCause.THORNS,
                    getDamage(random, level).toFloat(),
                    0f
                )
            )
        }
    }

    override fun canEnchant(item: Item): Boolean {
        return item !is ItemElytra && super.canEnchant(item)
    }

    companion object {
        private fun shouldHit(random: ThreadLocalRandom, level: Int): Boolean {
            return level > 0 && random.nextFloat() < 0.15 * level
        }

        private fun getDamage(random: ThreadLocalRandom, level: Int): Int {
            return if (level > 10) level - 10 else random.nextInt(1, 5)
        }
    }
}
