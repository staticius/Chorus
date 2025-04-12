package org.chorus.item

import org.chorus.Player
import org.chorus.Server
import org.chorus.entity.*
import org.chorus.entity.Entity.Companion.createEntity
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.entity.projectile.abstract_arrow.EntityArrow
import org.chorus.event.entity.EntityShootBowEvent
import org.chorus.event.entity.ProjectileLaunchEvent
import org.chorus.item.enchantment.*
import org.chorus.item.enchantment.bow.EnchantmentBow
import org.chorus.level.Sound
import org.chorus.level.Transform
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.FloatTag
import org.chorus.nbt.tag.ListTag
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class ItemBow @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.BOW, meta, count, "Bow") {
    override val maxDurability: Int
        get() = DURABILITY_BOW

    override val enchantAbility: Int
        get() = 1

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        return player.isCreative ||
                player.getInventory().contents.values.stream().filter { item: Item? -> item is ItemArrow }
                    .findFirst().isPresent ||
                player.getOffhandInventory()!!.contents.values.stream().filter { item: Item? -> item is ItemArrow }
                    .findFirst().isPresent
    }

    override fun onRelease(player: Player, ticksUsed: Int): Boolean {
        val inventoryOptional = player.getInventory().contents.entries.stream()
            .filter { item: Map.Entry<Int?, Item?> -> item.value is ItemArrow }.findFirst()
        val offhandOptional = player.getOffhandInventory()!!.contents.entries.stream()
            .filter { item: Map.Entry<Int?, Item?> -> item.value is ItemArrow }.findFirst()


        if (offhandOptional.isEmpty && inventoryOptional.isEmpty && (player.isAdventure || player.isSurvival)) {
            player.getOffhandInventory()!!.sendContents(player)
            player.getInventory().sendContents(player)
            return false
        }

        var damage = 2.0

        val bowDamage = this.getEnchantment(Enchantment.Companion.ID_BOW_POWER)
        if (bowDamage != null && bowDamage.level > 0) {
            damage += bowDamage.level.toDouble() * 0.5 + 0.5
        }

        val flameEnchant = this.getEnchantment(Enchantment.Companion.ID_BOW_FLAME)
        val flame = flameEnchant != null && flameEnchant.level > 0

        var arrowTransform: Transform = player.transform
        val directionVector = player.getDirectionVector().multiply(1.1)
        arrowTransform = arrowTransform.add(directionVector.x, 0.0, directionVector.z)
        arrowTransform.setY(player.position.y + player.getEyeHeight() + directionVector.y)

        val itemArrow =
            (if (offhandOptional.isPresent) offhandOptional.get().value else if (inventoryOptional.isPresent) inventoryOptional.get().value else ItemArrow()) as ItemArrow


        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(arrowTransform.position.x))
                    .add(FloatTag(arrowTransform.position.y))
                    .add(FloatTag(arrowTransform.position.z))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(-sin(player.rotation.yaw / 180 * Math.PI) * cos(player.rotation.pitch / 180 * Math.PI)))
                    .add(FloatTag(-sin(player.rotation.pitch / 180 * Math.PI)))
                    .add(FloatTag(cos(player.rotation.yaw / 180 * Math.PI) * cos(player.rotation.pitch / 180 * Math.PI)))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag((if (player.rotation.yaw > 180) 360 else 0) - player.rotation.yaw.toFloat()))
                    .add(FloatTag(-player.rotation.pitch.toFloat()))
            )
            .putShort("Fire", if (flame) 45 * 60 else 0)
            .putDouble("damage", damage)

        val p = ticksUsed.toDouble() / 20
        val maxForce = 3.5
        val f = min((p * p + p * 2) / 3, 1.0) * maxForce

        val arrow = createEntity(EntityID.ARROW, player.chunk!!, nbt, player, f == maxForce) as EntityArrow? ?: return false
        val copy = itemArrow.clone() as ItemArrow
        copy.setCount(1)
        arrow.setItem(copy)
        val entityShootBowEvent = EntityShootBowEvent(player, this, arrow, f)

        if (f < 0.1 || ticksUsed < 3) {
            entityShootBowEvent.setCancelled()
        }

        Server.instance.pluginManager.callEvent(entityShootBowEvent)
        if (entityShootBowEvent.isCancelled) {
            entityShootBowEvent.getProjectile().kill()
            player.getInventory().sendContents(player)
            player.getOffhandInventory()!!.sendContents(player)
        } else {
            entityShootBowEvent.getProjectile()
                .setMotion(entityShootBowEvent.getProjectile().getMotion().multiply(entityShootBowEvent.force))
            val infinityEnchant = this.getEnchantment(Enchantment.Companion.ID_BOW_INFINITY)
            val infinity = infinityEnchant != null && infinityEnchant.level > 0
            val projectile = entityShootBowEvent.getProjectile()
            if (infinity && (projectile is EntityArrow)) {
                projectile.setPickupMode(EntityProjectile.PICKUP_CREATIVE)
            }

            for (enc in this.enchantments) {
                if (enc is EnchantmentBow) {
                    enc.onBowShoot(player, arrow, this)
                }
            }

            if (player.isAdventure || player.isSurvival) {
                if (!infinity) {
                    if (offhandOptional.isPresent) {
                        val index = offhandOptional.get().key
                        player.getOffhandInventory()!!
                            .setItem(index, player.getOffhandInventory()!!.getItem(index).decrement(1))
                    } else {
                        val index = inventoryOptional.get().key
                        player.getInventory().setItem(index, player.getInventory().getItem(index).decrement(1))
                    }
                }
                if (!this.isUnbreakable) {
                    val durability = this.getEnchantment(Enchantment.Companion.ID_DURABILITY)
                    if (!(durability != null && durability.level > 0 && (100 / (durability.level + 1)) <= Random().nextInt(
                            100
                        ))
                    ) {
                        this.damage = this.damage + 1
                        if (this.damage >= this.maxDurability) {
                            player.level!!.addSound(player.position, Sound.RANDOM_BREAK)
                            count--
                        }
                        player.getInventory().setItemInHand(this)
                    }
                }
            }
            if (entityShootBowEvent.getProjectile() != null) {
                val projectev = ProjectileLaunchEvent(entityShootBowEvent.getProjectile(), player)
                Server.instance.pluginManager.callEvent(projectev)
                if (projectev.isCancelled) {
                    entityShootBowEvent.getProjectile().kill()
                } else {
                    entityShootBowEvent.getProjectile().spawnToAll()
                    player.level!!.addSound(player.position, Sound.RANDOM_BOW)
                }
            }
        }

        return true
    }
}
