package org.chorus.entity

import cn.nukkit.Player
import cn.nukkit.block.BlockID
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.event.entity.EntityDamageEvent.DamageModifier
import cn.nukkit.inventory.*
import cn.nukkit.item.*
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.NukkitMath
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
import java.util.concurrent.*
import java.util.function.Predicate

abstract class EntityHumanType(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), IHuman {
    @JvmField
    protected var inventory: HumanInventory? = null
    protected var enderChestInventory: HumanEnderChestInventory? = null
    @JvmField
    protected var offhandInventory: HumanOffHandInventory? = null

    override fun getInventory(): HumanInventory {
        return inventory!!
    }

    override fun getOffhandInventory(): HumanOffHandInventory? {
        return offhandInventory
    }

    override fun getEnderChestInventory(): HumanEnderChestInventory? {
        return enderChestInventory
    }

    override fun setInventories(inventory: Array<Inventory>) {
        this.inventory = inventory.get(0) as HumanInventory?
        this.offhandInventory = inventory.get(1) as HumanOffHandInventory?
        this.enderChestInventory = inventory.get(2) as HumanEnderChestInventory?
    }

    override fun getDrops(): Array<Item?> {
        if (this.inventory != null) {
            val drops: MutableList<Item> = ArrayList<Item>(
                inventory!!.getContents().values()
            )
            drops.addAll(offhandInventory!!.getContents().values())
            return drops.stream().filter(Predicate { item: Item -> !item.keepOnDeath() }).toList()
                .toArray(Item.EMPTY_ARRAY)
        }
        return Item.EMPTY_ARRAY
    }

    override fun asyncPrepare(currentTick: Int) {}

    override fun attack(source: EntityDamageEvent): Boolean {
        if (this.isClosed() || !this.isAlive()) {
            return false
        }

        if (source.cause != DamageCause.VOID && source.cause != DamageCause.CUSTOM && source.cause != DamageCause.MAGIC && source.cause != DamageCause.HUNGER) {
            var armorPoints: Int = 0
            var epf: Int = 0

            //            int toughness = 0;
            for (armor: Item in inventory!!.getArmorContents()) {
                armorPoints += armor.getArmorPoints()
                epf = (epf + calculateEnchantmentProtectionFactor(armor, source)).toInt()
                //toughness += armor.getToughness();
            }

            if (source.canBeReducedByArmor()) {
                source.setDamage(-source.getFinalDamage() * armorPoints * 0.04f, DamageModifier.ARMOR)
            }

            source.setDamage(
                -source.getFinalDamage() * Math.min(
                    NukkitMath.ceilFloat(
                        Math.min(
                            epf,
                            25
                        ) * (ThreadLocalRandom.current().nextInt(50, 100).toFloat() / 100)
                    ), 20
                ) * 0.04f,
                DamageModifier.ARMOR_ENCHANTMENTS
            )

            source.setDamage(-Math.min(this.getAbsorption(), source.getFinalDamage()), DamageModifier.ABSORPTION)
        }

        if (super.attack(source)) {
            var damager: Entity? = null

            if (source is EntityDamageByEntityEvent) {
                damager = source.damager
            }

            for (slot in 0..3) {
                val armorOld: Item = inventory!!.getArmorItem(slot)
                if (armorOld.isArmor()) {
                    val armor: Item = damageArmor(armorOld, damager, source)
                    inventory!!.setArmorItem(slot, armor, armor.getId() !== BlockID.AIR)
                }
            }

            return true
        } else {
            return false
        }
    }

    override fun calculateEnchantmentProtectionFactor(item: Item, source: EntityDamageEvent?): Double {
        if (!item.hasEnchantments()) {
            return 0.0
        }

        var epf: Double = 0.0

        if (item.applyEnchantments()) {
            for (ench: Enchantment in item.getEnchantments()) {
                epf += ench.getProtectionFactor(source).toDouble()
            }
        }

        return epf
    }

    override fun setOnFire(seconds: Int) {
        var seconds: Int = seconds
        var level: Int = 0

        for (armor: Item in inventory!!.getArmorContents()) {
            val fireProtection: Enchantment? = armor.getEnchantment(Enchantment.ID_PROTECTION_FIRE)

            if (fireProtection != null && fireProtection.getLevel() > 0) {
                level = Math.max(level, fireProtection.getLevel())
            }
        }

        seconds = (seconds * (1 - level * 0.15)).toInt()

        super.setOnFire(seconds)
    }

    override fun playerApplyNameTag(player: Player, item: Item): Boolean {
        return false
    }

    protected fun damageArmor(armor: Item, damager: Entity?, event: EntityDamageEvent): Item {
        if (armor.hasEnchantments()) {
            if (damager != null) {
                if (armor.applyEnchantments()) {
                    for (enchantment: Enchantment in armor.getEnchantments()) {
                        enchantment.doPostAttack(damager, this)
                    }
                }
            }

            val durability: Enchantment? = armor.getEnchantment(Enchantment.ID_DURABILITY)
            if (durability != null && durability.getLevel() > 0 && (100 / (durability.getLevel() + 1)) <= Utils.random.nextInt(
                    100
                )
            ) {
                return armor
            }
        }

        if (event.cause != DamageCause.VOID && event.cause != DamageCause.MAGIC && event.cause != DamageCause.HUNGER && event.cause != DamageCause.DROWNING && event.cause != DamageCause.SUFFOCATION && event.cause != DamageCause.SUICIDE && event.cause != DamageCause.FIRE_TICK && event.cause != DamageCause.FALL) { // No armor damage

            if (armor.isUnbreakable() || armor.getMaxDurability() < 0) {
                return armor
            }

            if (armor is ItemShield) armor.setDamage(
                armor.getDamage() + (if (event.getDamage() >= 3) event.getDamage().toInt() + 1 else 0)
            )
            else armor.setDamage(armor.getDamage() + Math.max(1, (event.getDamage() / 4.0f).toInt()))

            if (armor.getDamage() >= armor.getMaxDurability()) {
                level!!.addSound(this.position, Sound.RANDOM_BREAK)
                return Item.get(BlockID.AIR, 0, 0)
            }
        }

        return armor
    }

    override fun getNetworkId(): Int {
        return IHuman.Companion.NETWORK_ID
    }

    override fun getIdentifier(): String {
        return EntityID.Companion.PLAYER
    }
}
