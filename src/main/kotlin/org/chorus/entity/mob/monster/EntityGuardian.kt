package org.chorus.entity.mob.monster

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.*
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
import org.chorus.entity.ai.executor.FleeFromTargetExecutor
import org.chorus.entity.ai.executor.GuardianAttackExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.executor.SpaceRandomRoamExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.SwimmingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus.entity.mob.EntityMob
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.*
import java.util.List
import java.util.Set
import java.util.concurrent.*
import java.util.function.Function

/**
 * @author PikyCZ
 */
class EntityGuardian(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntitySwimmable {
    override fun getIdentifier(): String {
        return EntityID.GUARDIAN
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            setOf<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_GUARDIAN_AMBIENT, 0.8f, 1.2f, 1f, 1f),
                    all(IBehaviorEvaluator { entity: EntityMob? -> isInsideOfWater() }, RandomSoundEvaluator()),
                    6,
                    1,
                    1,
                    true
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_GUARDIAN_LAND_IDLE, 0.8f, 1.2f, 1f, 1f),
                    all(IBehaviorEvaluator { entity: EntityMob? -> !isInsideOfWater() }, RandomSoundEvaluator()),
                    5,
                    1,
                    1,
                    true
                ),
                Behavior(
                    FleeFromTargetExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.5f, true, 9f), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 100)
                    ), 4, 1
                ),
                Behavior(
                    GuardianAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 15, true, 60, 40), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.memoryStorage.get(CoreMemoryTypes.Companion.NEAREST_PLAYER) != null && !entity.memoryStorage
                                .get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER)!!.isBlocking()
                        },
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.memoryStorage.get(CoreMemoryTypes.Companion.NEAREST_PLAYER) != null && level!!.raycastBlocks(
                                entity.position, entity.memoryStorage
                                    .get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER)!!.position
                            ).stream().allMatch { obj: Block -> obj.isTransparent }
                        }
                    ), 3, 1),
                Behavior(
                    GuardianAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        15,
                        true,
                        60,
                        40
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(SpaceRandomRoamExecutor(0.36f, 12, 1, 80, false, -1, false, 10), none(), 1, 1)
            ),
            setOf<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    listOf(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    })
            ),
            setOf<IController>(SpaceMoveController(), LookController(true, true), DiveController()),
            SimpleSpaceAStarRouteFinder(SwimmingPosEvaluator(), this),
            this
        )
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source.cause == DamageCause.SUFFOCATION) {
            return false
        }
        if (super.attack(source)) {
            if (source is EntityDamageByEntityEvent) {
                source.damager.attack(
                    EntityDamageByEntityEvent(
                        this,
                        source.entity!!,
                        DamageCause.THORNS,
                        (if (Server.instance.getDifficulty() == 3) 2 else 3).toFloat()
                    )
                )
            }
            return true
        }
        return false
    }

    public override fun initEntity() {
        this.maxHealth = 30
        this.diffHandDamage = floatArrayOf(4f, 6f, 9f)
        super.initEntity()
    }

    override fun getOriginalName(): String {
        return "Guardian"
    }

    override fun getWidth(): Float {
        return 0.85f
    }

    override fun getHeight(): Float {
        return 0.85f
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getDrops(): Array<Item> {
        val secondLoot = ThreadLocalRandom.current().nextInt(6)
        return arrayOf(
            Item.get(ItemID.PRISMARINE_SHARD, 0, Utils.rand(0, 2)),
            if (ThreadLocalRandom.current().nextInt(1000) <= 25) Item.get(ItemID.COD, 0, 1) else Item.AIR,
            if (secondLoot <= 2) Item.get(ItemID.COD, 0, Utils.rand(0, 1)) else Item.AIR,
            if (secondLoot > 2 && secondLoot <= 4) Item.get(ItemID.PRISMARINE_CRYSTALS, 0, Utils.rand(0, 1)) else Item.AIR
        )
    }

    override fun getExperienceDrops(): Int {
        return 10
    }

    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.SQUID, EntityID.GLOW_SQUID, EntityID.AXOLOTL -> true
            else -> false
        }
    }
}
