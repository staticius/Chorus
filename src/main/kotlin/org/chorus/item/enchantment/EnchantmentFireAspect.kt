package org.chorus.item.enchantment

import org.chorus.Player
import org.chorus.Server
import org.chorus.event.entity.EntityCombustByEntityEvent
import org.chorus.event.entity.EntityDamageByEntityEvent
import kotlin.math.max


class EnchantmentFireAspect :
    Enchantment(Enchantment.Companion.ID_FIRE_ASPECT, "fire", Rarity.RARE, EnchantmentType.SWORD) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 10 + (level - 1) * 20
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return super.getMinEnchantAbility(level) + 50
    }

    override val maxLevel: Int
        get() = 2

    override fun doAttack(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val attacker = event.damager
        if ((entity !is Player || !entity.isCreative)) {
            val duration = max((entity!!.fireTicks / 20).toDouble(), (getLevel() shl 2).toDouble()).toInt()

            val ev = EntityCombustByEntityEvent(attacker, entity, duration)
            Server.getInstance().pluginManager.callEvent(ev)

            if (!ev.isCancelled) {
                entity.setOnFire(ev.duration)
            }
        }
    }
}
