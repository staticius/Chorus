package cn.nukkit.entity.mob.animal

import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntityWalkable
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import cn.nukkit.entity.ai.evaluator.PassByTimeEvaluator
import cn.nukkit.entity.ai.evaluator.ProbabilityEvaluator
import cn.nukkit.entity.ai.executor.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestFeedingPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import java.util.Set

/**
 * @author BeYkeRYkt (Nukkit Project)
 */
class EntityChicken(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.CHICKEN
    }


    override fun updateMovement() {
        //补充鸡的缓慢无伤落地特性
        if (!this.onGround && motion.up < -0.08f) {
            motion.up = -0.08
            this.highestPosition = position.up
        }
        super.updateMovement()
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>( //用于刷新InLove状态的核心行为
                Behavior(
                    InLoveExecutor(400),
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, 0, 400),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, 6000, Int.MAX_VALUE)
                    ),
                    1, 1
                ),  //生长
                Behavior(
                    AnimalGrowExecutor(),  //todo：Growth rate
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.ENTITY_SPAWN_TIME, 20 * 60 * 20, Int.MAX_VALUE),
                        IBehaviorEvaluator { entity: EntityMob -> entity is EntityAnimal && entity.isBaby() }
                    ),
                    1, 1, 1200
                )
            ),
            Set.of<IBehavior>(
                Behavior(
                    FlatRandomRoamExecutor(0.22f, 12, 40, true, 100, true, 10),
                    PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 100),
                    6,
                    1
                ),
                Behavior(
                    EntityBreedingExecutor<EntityChicken>(EntityChicken::class.java, 16, 100, 0.3f),
                    { entity: EntityMob -> entity.memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE) },
                    5,
                    1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER, 0.22f, true),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER),
                    4,
                    1
                ),
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100),
                    ProbabilityEvaluator(4, 10),
                    1,
                    1,
                    100
                ),
                Behavior(
                    FlatRandomRoamExecutor(0.22f, 12, 100, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
                ),
                Behavior(
                    { entity: EntityMob ->
                        entity.memoryStorage!!.put<Int>(
                            CoreMemoryTypes.Companion.LAST_EGG_SPAWN_TIME,
                            level!!.tick
                        )
                        entity.level!!.dropItem(entity.position, Item.get(Item.EGG))
                        entity.level!!.addSound(entity.position, Sound.MOB_CHICKEN_PLOP)
                        false
                    }, any(
                        all(
                            PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_EGG_SPAWN_TIME, 6000, 12000),
                            ProbabilityEvaluator(20, 100)
                        ),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_EGG_SPAWN_TIME, 12000, Int.MAX_VALUE)
                    ), 1, 1, 20
                )
            ),
            Set.of<ISensor>(NearestFeedingPlayerSensor(8.0, 0.0), NearestPlayerSensor(8.0, 0.0, 20)),
            Set.of<IController>(WalkController(), LookController(true, true), FluctuateController()),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun getWidth(): Float {
        if (this.isBaby()) {
            return 0.3f
        }
        return 0.6f
    }

    override fun getHeight(): Float {
        if (this.isBaby()) {
            return 0.4f
        }
        return 0.8f
    }

    override fun getOriginalName(): String {
        return "Chicken"
    }

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get((if (this.isOnFire) Item.COOKED_CHICKEN else Item.CHICKEN)), Item.get(Item.FEATHER))
    }


    override fun initEntity() {
        this.maxHealth = 4
        super.initEntity()
    }

    override fun isBreedingItem(item: Item): Boolean {
        val id = item.id

        return id === Item.WHEAT_SEEDS || id === Item.MELON_SEEDS || id === Item.PUMPKIN_SEEDS || id === Item.BEETROOT_SEEDS
    }
}
