package cn.nukkit.entity.ai.executor

import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.monster.EntityMonster
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.item.*
import cn.nukkit.plugin.InternalPlugin
import java.util.*
import java.util.function.Consumer

class UsePotionExecutor
/**
 *
 * @param speed             <br></br>The speed of movement towards the attacking target
 * @param coolDownTick      <br></br>Attack cooldown (tick)
 * @param useDelay          <br></br>Attack Animation time(tick)
 */(protected var speed: Float, protected val coolDownTick: Int, protected val useDelay: Int) : EntityControl,
    IBehaviorExecutor {
    private var tick1 = 0 //control the coolDownTick
    private var tick2 = 0 //control the pullBowTick

    override fun execute(entity: EntityMob): Boolean {
        if (tick2 == 0) {
            tick1++
        }
        if (!entity.isEnablePitch) entity.isEnablePitch = true

        if (entity.movementSpeed != speed) entity.movementSpeed = speed

        setRouteTarget(entity, null)

        if (tick2 == 0 && tick1 > coolDownTick) {
            this.tick1 = 0
            tick2++
            startShootSequence(entity)
        } else if (tick2 != 0) {
            tick2++
            if (tick2 > useDelay) {
                entity.level!!.scheduler.scheduleDelayedTask(
                    InternalPlugin.INSTANCE,
                    { endShootSequence(entity) }, 20
                )
                tick2 = 0
                return true
            }
        }
        return true
    }

    override fun onStop(entity: EntityMob) {
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        entity.isEnablePitch = false
        endShootSequence(entity)
    }

    override fun onInterrupt(entity: EntityMob) {
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        entity.isEnablePitch = false
        endShootSequence(entity)
    }


    private fun startShootSequence(entity: Entity) {
        if (entity is EntityMonster) {
            entity.setItemInHand(getPotion(entity))
        }
    }

    private fun endShootSequence(entity: Entity) {
        if (entity is EntityMonster) {
            val item = entity.itemInHand
            if (item is ItemPotion) {
                PotionType.Companion.get(item.getDamage()).getEffects(false)!!
                    .forEach(Consumer<Effect?> { effect: Effect? -> entity.addEffect(effect) })
            }
            entity.setItemInHand(Item.AIR)
        }
    }

    fun getPotion(entity: Entity): Item {
        if (entity.isInsideOfWater && !entity.hasEffect(EffectType.Companion.WATER_BREATHING)) {
            return ItemPotion.fromPotion(PotionType.Companion.WATER_BREATHING)
        } else if (!entity.hasEffect(EffectType.Companion.FIRE_RESISTANCE) && (entity.isOnFire || Arrays.stream<Block>(
                entity.level!!.getCollisionBlocks(
                    entity.getBoundingBox()!!.getOffsetBoundingBox(0.0, -1.0, 0.0)
                )
            ).anyMatch { block: Block? -> block is BlockMagma })
        ) {
            return ItemPotion.fromPotion(PotionType.Companion.FIRE_RESISTANCE)
        } else if (entity.health < entity.maxHealth) {
            return ItemPotion.fromPotion(PotionType.Companion.HEALING)
        } else if (entity is EntityMob) {
            if (entity.memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.BE_ATTACKED_EVENT)) {
                val event = entity.memoryStorage!!.get<EntityDamageEvent>(CoreMemoryTypes.Companion.BE_ATTACKED_EVENT)
                if (event is EntityDamageByEntityEvent) {
                    if (event.damager.position.distance(entity.position) > 11) {
                        return ItemPotion.fromPotion(PotionType.Companion.SWIFTNESS)
                    }
                }
            }
        }
        return Item.AIR
    }
}
