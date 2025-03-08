package cn.nukkit.item.enchantment.damage

import cn.nukkit.entity.*
import cn.nukkit.entity.effect.Effect.Companion.get
import cn.nukkit.entity.effect.EffectType
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.item.enchantment.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author MagicDroidX (Nukkit Project)
 */
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
