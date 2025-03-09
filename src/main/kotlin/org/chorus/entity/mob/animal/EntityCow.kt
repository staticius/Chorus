package org.chorus.entity.mob.animal

import cn.nukkit.Player
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
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
import java.util.Set

/**
 * @author BeYkeRYkt (Nukkit Project)
 */
class EntityCow(chunk: IChunk?, nbt: CompoundTag?) : EntityAnimal(chunk, nbt!!), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.COW
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
                )
            ),
            Set.of<IBehavior>(
                Behavior(
                    FlatRandomRoamExecutor(0.4f, 12, 40, true, 100, true, 10),
                    PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 100),
                    4,
                    1
                ),
                Behavior(
                    EntityBreedingExecutor<EntityCow>(EntityCow::class.java, 16, 100, 0.5f),
                    { entity: EntityMob -> entity.memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE) },
                    3,
                    1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER, 0.4f, true),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER),
                    2,
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
                    FlatRandomRoamExecutor(0.2f, 12, 100, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
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
            return 0.45f
        }
        return 0.9f
    }

    override fun getHeight(): Float {
        if (this.isBaby()) {
            return 0.65f
        }
        return 1.3f
    }

    override fun getOriginalName(): String {
        return "Cow"
    }

    override fun getDrops(): Array<out Item>? {
        if (!this.isBaby()) {
            return arrayOf(
                Item.get(Item.LEATHER, 0, Utils.rand(0, 2)),
                Item.get((if (this.isOnFire) Item.COOKED_BEEF else Item.BEEF), 0, Utils.rand(1, 3))
            )
        }
        return Item.EMPTY_ARRAY
    }


    override fun initEntity() {
        this.maxHealth = 10
        super.initEntity()
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (super.onInteract(player, item, clickedPos)) {
            return true
        }

        if (item.id === Item.BUCKET && item.damage == 0) {
            item.count--
            player.inventory.addItem(Item.get(Item.BUCKET, 1))
            return true
        }

        return false
    }
}
