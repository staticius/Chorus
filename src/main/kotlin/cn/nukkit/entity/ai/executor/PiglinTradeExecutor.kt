package cn.nukkit.entity.ai.executor

import cn.nukkit.block.*
import cn.nukkit.entity.EntityEquipment
import cn.nukkit.entity.EntityLiving
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.monster.EntityMonster
import cn.nukkit.item.*
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.Sound
import java.util.concurrent.*

class PiglinTradeExecutor : EntityControl, IBehaviorExecutor {
    protected var tick: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        tick++
        if (tick < 160) {
            if (tick % 30 == 0 && ThreadLocalRandom.current().nextBoolean()) {
                entity.level!!.addSound(entity.position, Sound.MOB_PIGLIN_ADMIRING_ITEM)
            }
            return true
        } else {
            if (entity is EntityMonster) {
                val offhand = entity.getItemInOffhand()
                if (offhand is ItemGoldIngot && !entity.isBaby()) {
                    entity.equipment.decreaseCount(EntityEquipment.Companion.OFF_HAND)
                    val motion = entity.getDirectionVector().multiply(0.4)
                    entity.level!!.dropItem(entity.position.add(0.0, 1.3, 0.0), drop, motion, 40)
                }
            }
            return false
        }
    }


    override fun onStart(entity: EntityMob) {
        tick = -1
        removeLookTarget(entity)
        entity.setDataFlag(EntityFlag.ADMIRING)
    }

    override fun onStop(entity: EntityMob) {
        if (entity is EntityMonster) {
            clearOffhand(entity, entity.getItemInOffhand())
        }
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        entity.isEnablePitch = false
        entity.setDataFlag(EntityFlag.ADMIRING, false)
    }

    val drop: Item
        get() {
            val item: Item
            val random = ThreadLocalRandom.current().nextInt(459)
            if (random < 5) {
                item = Item.get(Item.ENCHANTED_BOOK)
                item.addEnchantment(
                    Enchantment.get(Enchantment.ID_SOUL_SPEED)
                        .setLevel(ThreadLocalRandom.current().nextInt(1, 3))
                )
            } else if (random < 13) {
                item = Item.get(Item.IRON_BOOTS)
                item.addEnchantment(
                    Enchantment.get(Enchantment.ID_SOUL_SPEED)
                        .setLevel(ThreadLocalRandom.current().nextInt(1, 3))
                )
            } else if (random < 21) {
                item = Item.get(
                    Item.SPLASH_POTION,
                    EffectType.Companion.FIRE_RESISTANCE.id
                )
            } else if (random < 29) {
                item = Item.get(
                    Item.POTION,
                    EffectType.Companion.FIRE_RESISTANCE.id
                )
            } else if (random < 39) {
                item = Item.get(Item.POTION)
            } else if (random < 49) {
                item = Item.get(
                    Item.IRON_NUGGET,
                    0,
                    ThreadLocalRandom.current().nextInt(10, 37)
                )
            } else if (random < 59) {
                item = Item.get(
                    Item.ENDER_PEARL,
                    0,
                    ThreadLocalRandom.current().nextInt(2, 5)
                )
            } else if (random < 79) {
                item = Item.get(
                    Item.STRING,
                    0,
                    ThreadLocalRandom.current().nextInt(3, 10)
                )
            } else if (random < 99) {
                item = Item.get(
                    Item.QUARTZ,
                    0,
                    ThreadLocalRandom.current().nextInt(5, 13)
                )
            } else if (random < 139) {
                item = Item.get(Block.OBSIDIAN)
            } else if (random < 179) {
                item = Item.get(
                    Block.CRYING_OBSIDIAN,
                    0,
                    ThreadLocalRandom.current().nextInt(1, 4)
                )
            } else if (random < 219) {
                item = Item.get(Item.FIRE_CHARGE)
            } else if (random < 259) {
                item = Item.get(
                    Item.LEATHER,
                    0,
                    ThreadLocalRandom.current().nextInt(2, 5)
                )
            } else if (random < 299) {
                item = Item.get(
                    Block.SOUL_SAND,
                    0,
                    ThreadLocalRandom.current().nextInt(2, 9)
                )
            } else if (random < 339) {
                item = Item.get(
                    Item.NETHERBRICK,
                    0,
                    ThreadLocalRandom.current().nextInt(2, 9)
                )
            } else if (random < 379) {
                item = Item.get(
                    Item.ARROW,
                    0,
                    ThreadLocalRandom.current().nextInt(6, 12)
                )
            } else if (random < 419) {
                item = Item.get(
                    Block.GRAVEL,
                    0,
                    ThreadLocalRandom.current().nextInt(8, 17)
                )
            } else {
                item = Item.get(
                    Block.BLACKSTONE,
                    0,
                    ThreadLocalRandom.current().nextInt(8, 17)
                )
            }
            return item
        }

    override fun onInterrupt(entity: EntityMob) {
        onStop(entity)
    }

    fun clearOffhand(mob: EntityMonster, item: Item?) {
        mob.level!!.dropItem(mob.position, item) // TODO
        mob.equipment.clear(EntityEquipment.Companion.OFF_HAND)
    }
}


