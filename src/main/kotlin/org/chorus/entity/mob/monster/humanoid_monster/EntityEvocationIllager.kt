package org.chorus.entity.mob.monster.humanoid_monster

import org.chorus.Player
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.*
import org.chorus.entity.ai.executor.*
import org.chorus.entity.ai.executor.evocation.ColorConversionExecutor
import org.chorus.entity.ai.executor.evocation.FangCircleExecutor
import org.chorus.entity.ai.executor.evocation.FangLineExecutor
import org.chorus.entity.ai.executor.evocation.VexSummonExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.animal.EntitySheep
import org.chorus.entity.mob.monster.EntityCreaking
import org.chorus.entity.mob.monster.EntityVex
import org.chorus.item.*
import org.chorus.level.GameRule
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.DyeColor
import java.util.List
import java.util.Set
import java.util.concurrent.*
import java.util.function.Function

/**
 * @author PikyCZ
 */
class EntityEvocationIllager(chunk: IChunk?, nbt: CompoundTag?) : EntityIllager(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.EVOCATION_ILLAGER
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_EVOCATION_ILLAGER_AMBIENT), RandomSoundEvaluator(), 10, 1),
                Behavior(
                    FleeFromTargetExecutor(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY, 0.5f, true, 9f), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY, 8.0),
                        any(
                            MemoryCheckEmptyEvaluator(CoreMemoryTypes.Companion.LAST_MAGIC),
                            IBehaviorEvaluator { entity: EntityMob -> entity.memoryStorage!!.get<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC) == SPELL.NONE }
                        )
                    ), 9, 1),
                Behavior(
                    FleeFromTargetExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.5f,
                        true,
                        11f
                    ),
                    all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET, 10.0),
                        any(
                            MemoryCheckEmptyEvaluator(CoreMemoryTypes.Companion.LAST_MAGIC),
                            IBehaviorEvaluator { entity: EntityMob -> entity.memoryStorage!!.get<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC) == SPELL.NONE }
                        )
                    ),
                    8,
                    1),
                Behavior(
                    ColorConversionExecutor(), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_CONVERSION, 100),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, 40),
                        IBehaviorEvaluator { entity: EntityMob ->
                            for (entity1 in entity.level!!.getNearbyEntities(
                                entity.getBoundingBox()!!.grow(16.0, 16.0, 16.0)
                            )) {
                                if (entity1 is EntitySheep) {
                                    if (entity1.getColor() == DyeColor.BLUE.woolData) {
                                        return@all true
                                    }
                                }
                            }
                            false
                        },
                        any(
                            MemoryCheckEmptyEvaluator(CoreMemoryTypes.Companion.LAST_MAGIC),
                            IBehaviorEvaluator { entity: EntityMob -> entity.memoryStorage!!.get<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC) == SPELL.NONE },
                            IBehaviorEvaluator { entity: EntityMob -> entity.memoryStorage!!.get<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC) == SPELL.COLOR_CONVERSION }
                        ),
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.level!!.gameRules.getBoolean(GameRule.MOB_GRIEFING)
                        }
                    ), 7, 1),
                Behavior(
                    VexSummonExecutor(), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_SUMMON, 340),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, 40),
                        IBehaviorEvaluator { entity: EntityMob ->
                            var count = 0
                            for (entity1 in entity.level!!.getNearbyEntities(
                                entity.getBoundingBox()!!.grow(15.0, 15.0, 15.0)
                            )) {
                                if (entity1 is EntityVex) count++
                            }
                            count < 8
                        },
                        any(
                            MemoryCheckEmptyEvaluator(CoreMemoryTypes.Companion.LAST_MAGIC),
                            IBehaviorEvaluator { entity: EntityMob -> entity.memoryStorage!!.get<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC) == SPELL.NONE },
                            IBehaviorEvaluator { entity: EntityMob -> entity.memoryStorage!!.get<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC) == SPELL.SUMMON }
                        )
                    ), 6, 1),
                Behavior(
                    FangCircleExecutor(), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_CAST, 100),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, 40),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET, 3.0),
                        any(
                            MemoryCheckEmptyEvaluator(CoreMemoryTypes.Companion.LAST_MAGIC),
                            IBehaviorEvaluator { entity: EntityMob -> entity.memoryStorage!!.get<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC) == SPELL.NONE },
                            IBehaviorEvaluator { entity: EntityMob -> entity.memoryStorage!!.get<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC) == SPELL.CAST_CIRLCE }
                        )
                    ), 5, 1),
                Behavior(
                    FangLineExecutor(), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_CAST, 100),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, 40),
                        any(
                            MemoryCheckEmptyEvaluator(CoreMemoryTypes.Companion.LAST_MAGIC),
                            IBehaviorEvaluator { entity: EntityMob -> entity.memoryStorage!!.get<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC) == SPELL.NONE },
                            IBehaviorEvaluator { entity: EntityMob -> entity.memoryStorage!!.get<SPELL>(CoreMemoryTypes.Companion.LAST_MAGIC) == SPELL.CAST_LINE }
                        )
                    ), 4, 1),
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET, 1), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        IBehaviorEvaluator { entity: EntityMob -> !entity.getDataFlag(EntityFlag.CASTING) }
                    ), 3, 1),
                Behavior(
                    DoNothingExecutor(),
                    { entity: EntityMob -> entity.getDataFlag(EntityFlag.CASTING) }, 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    }),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY),
                    Function<Entity, Boolean> { entity: Entity? -> entity is EntityCreaking })
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun spawnTo(player: Player) {
        super.spawnTo(player)
    }

    override fun initEntity() {
        this.maxHealth = 24
        super.initEntity()
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Evoker"
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getExperienceDrops(): Int {
        return 10
    }

    override fun getDrops(): Array<Item?> {
        return arrayOf(
            Item.get(Item.TOTEM_OF_UNDYING),
            Item.get(Item.EMERALD, 0, ThreadLocalRandom.current().nextInt(2))
        )
    }


    enum class SPELL {
        NONE,
        CAST_LINE,
        CAST_CIRLCE,
        SUMMON,
        COLOR_CONVERSION
    }
}
