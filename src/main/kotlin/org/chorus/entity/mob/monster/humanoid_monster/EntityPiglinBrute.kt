package org.chorus.entity.mob.monster.humanoid_monster

import cn.nukkit.block.BlockDoor
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.PiglinTransformExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.item.*
import cn.nukkit.level.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import java.util.List
import java.util.Set
import java.util.function.Function

/**
 * @author joserobjr
 * @since 2020-11-20
 */
class EntityPiglinBrute(chunk: IChunk?, nbt: CompoundTag?) : EntityPiglin(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.PIGLIN_BRUTE
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    PiglinTransformExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob -> entity.level!!.dimension != Level.DIMENSION_NETHER },
                        IBehaviorEvaluator { entity: EntityMob? -> !isImmobile },
                        IBehaviorEvaluator { entity: EntityMob -> !entity.namedTag!!.getBoolean("IsImmuneToZombification") }
                    ), 12, 1),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_PIGLIN_ANGRY, 0.8f, 1.2f, 0.8f, 0.8f), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> isAngry() }), 10, 1
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_PIGLIN_AMBIENT, 0.8f, 1.2f, 0.8f, 0.8f), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> !isAngry() }), 9, 1
                ),
                Behavior(
                    PiglinMeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.5f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    6,
                    1
                ),
                Behavior(
                    PiglinMeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.5f, 40, false, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    5,
                    1
                ),
                Behavior(
                    PiglinFleeFromTargetExecutor(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY),
                        IBehaviorEvaluator { entity: EntityMob? -> !isBaby() }
                    ), 3, 1),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    }),
                NearestPlayerAngryPiglinSensor(),
                NearestEntitySensor(
                    EntityZombiePigman::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    8.0,
                    0.0
                ),
                BlockSensor(BlockDoor::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 2, 2, 20)
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        super.initEntity()
        this.maxHealth = 50
        this.diffHandDamage = floatArrayOf(6f, 10f, 15f)
        setItemInHand(Item.get(Item.GOLDEN_AXE))
    }

    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.Companion.WITHER_SKELETON, EntityID.Companion.WITHER -> true
            else -> false
        }
    }

    override fun getOriginalName(): String {
        return "Piglin Brute"
    }
}
