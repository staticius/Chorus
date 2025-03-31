package org.chorus.entity.mob.monster

import org.chorus.Player
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus.entity.ai.evaluator.PassByTimeEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.MeleeAttackExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.executor.SpaceRandomRoamExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.FlyingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.monster.humanoid_monster.EntityEvocationIllager
import org.chorus.entity.mob.villagers.EntityVillager
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag


import java.util.List
import java.util.Set
import java.util.concurrent.*
import java.util.function.Function

class EntityVex(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityFlyable {
    override fun getIdentifier(): String {
        return EntityID.VEX
    }


    var illager: EntityEvocationIllager? = null
    private val start_damage_timer = ThreadLocalRandom.current().nextInt(30, 120)


    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_VEX_AMBIENT), RandomSoundEvaluator(), 5, 1),
                Behavior(
                    VexMeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, 80, Int.MAX_VALUE)
                    ), 4, 1
                ),
                Behavior(
                    VexMeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        40,
                        true,
                        30
                    ), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, 80, Int.MAX_VALUE)
                    ), 3, 1
                ),
                Behavior(
                    VexMeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 40, false, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, 80, Int.MAX_VALUE)
                    ), 2, 1
                ),
                Behavior(
                    SpaceRandomRoamExecutor(0.15f, 12, 100, 20, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
                )
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(70.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 70.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    })
            ),
            Set.of<IController>(SpaceMoveController(), LookController(true, true), LiftController()),
            SimpleSpaceAStarRouteFinder(FlyingPosEvaluator(), this),
            this
        )
    }

    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.Companion.VILLAGER -> entity is EntityVillager && !entity.isBaby()
            EntityID.Companion.IRON_GOLEM, EntityID.Companion.WANDERING_TRADER -> true
            else -> false
        }
    }

    override fun initEntity() {
        this.maxHealth = 14
        super.initEntity()
        memoryStorage!!.set<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, level!!.tick)
        this.setItemInHand(Item.get(Item.IRON_SWORD))
        this.diffHandDamage = floatArrayOf(5.5f, 9f, 13.5f)
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source.cause == DamageCause.SUFFOCATION) {
            return false
        }
        return super.attack(source)
    }

    override fun getWidth(): Float {
        return 0.4f
    }

    override fun getHeight(): Float {
        return 0.8f
    }

    override fun getOriginalName(): String {
        return "Vex"
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getDrops(): Array<Item> {
        if (itemInHand is ItemTool) {
            tool.setDamage(ThreadLocalRandom.current().nextInt(tool.getMaxDurability()))
            return arrayOf<Item?>(
                tool
            )
        }
        return super.getDrops()
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (closed) return true
        if (getIllager() != null) {
            if (position.distanceSquared(illager!!.position) > 56.25) {
                moveTarget = illager.position
                lookTarget = illager.position
            }
            if (ticksLived % 20 == 0) {
                if (ticksLived >= start_damage_timer * 20) {
                    this.attack(EntityDamageEvent(this, DamageCause.AGE, 1f))
                }
            }
        }
        return super.onUpdate(currentTick)
    }

    private inner class VexMeleeAttackExecutor(
        memory: MemoryType<out Entity?>?,
        speed: Float,
        maxSenseRange: Int,
        clearDataWhenLose: Boolean,
        coolDown: Int
    ) :
        MeleeAttackExecutor(memory, speed, maxSenseRange, clearDataWhenLose, coolDown, 1f) {
        override fun onStart(entity: EntityMob) {
            super.onStart(entity)
            entity.setDataFlag(EntityFlag.CHARGING)
            entity.level!!.addSound(entity.position, Sound.MOB_VEX_CHARGE)
        }

        override fun onStop(entity: EntityMob) {
            super.onStop(entity)
            entity.setDataFlag(EntityFlag.CHARGING, false)
        }

        override fun onInterrupt(entity: EntityMob) {
            super.onInterrupt(entity)
            entity.setDataFlag(EntityFlag.CHARGING, false)
        }
    }
}
