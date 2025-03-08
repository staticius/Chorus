package cn.nukkit.entity.mob.monster.humanoid_monster

import cn.nukkit.Player
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.*
import cn.nukkit.entity.ai.executor.*
import cn.nukkit.entity.ai.executor.evocation.ColorConversionExecutor
import cn.nukkit.entity.ai.executor.evocation.FangCircleExecutor
import cn.nukkit.entity.ai.executor.evocation.FangLineExecutor
import cn.nukkit.entity.ai.executor.evocation.VexSummonExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestTargetEntitySensor
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.animal.EntitySheep
import cn.nukkit.entity.mob.monster.EntityCreaking
import cn.nukkit.entity.mob.monster.EntityVex
import cn.nukkit.item.*
import cn.nukkit.level.GameRule
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.DyeColor
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
