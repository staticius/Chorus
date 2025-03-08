package cn.nukkit.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.entity.effect.EffectType
import cn.nukkit.item.enchantment.Enchantment

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class EntityDamageByEntityEvent : EntityDamageEvent {
    @JvmField
    val damager: Entity

    var knockBack: Float
    private var enchantments: Array<Enchantment>?

    @JvmOverloads
    constructor(damager: Entity, entity: Entity, cause: DamageCause, damage: Float, knockBack: Float = 0.3f) : super(
        entity,
        cause,
        damage
    ) {
        this.damager = damager
        this.knockBack = knockBack
        this.addAttackerModifiers(damager)
    }

    @JvmOverloads
    constructor(
        damager: Entity,
        entity: Entity,
        cause: DamageCause,
        modifiers: Map<DamageModifier?, Float?>,
        knockBack: Float = 0.3f,
        enchantments: Array<Enchantment>? = null
    ) : super(entity, cause, modifiers) {
        this.damager = damager
        this.knockBack = knockBack
        this.enchantments = enchantments
        this.addAttackerModifiers(damager)
    }

    protected fun addAttackerModifiers(damager: Entity) {
        if (damager.hasEffect(EffectType.STRENGTH)) {
            this.setDamage(
                (this.getDamage(DamageModifier.BASE) * 0.3 * damager.getEffect(EffectType.STRENGTH)
                    .getLevel()).toFloat(), DamageModifier.STRENGTH
            )
        }

        if (damager.hasEffect(EffectType.WEAKNESS)) {
            this.setDamage(
                -(this.getDamage(DamageModifier.BASE) * 0.2 * damager.getEffect(EffectType.WEAKNESS)
                    .getLevel()).toFloat(), DamageModifier.WEAKNESS
            )
        }
    }

    val weaponEnchantments: Array<Enchantment>?
        get() {
            if (enchantments == null) {
                return null
            }
            return if (enchantments!!.size > 0) enchantments!!.clone() else Enchantment.EMPTY_ARRAY
        }
}
