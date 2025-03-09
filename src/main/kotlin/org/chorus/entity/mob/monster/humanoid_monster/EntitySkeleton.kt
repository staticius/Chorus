package org.chorus.entity.mob.monster.humanoid_monster

import cn.nukkit.Player
import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntitySmite
import cn.nukkit.entity.EntityWalkable
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.BowShootExecutor
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestEntitySensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.mob.EntityGolem
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import java.util.Set

/**
 * @author PikyCZ
 */
open class EntitySkeleton(chunk: IChunk?, nbt: CompoundTag?) : EntityHumanoidMonster(chunk, nbt), EntityWalkable,
    EntitySmite {
    override fun getIdentifier(): String {
        return EntityID.Companion.SKELETON
    }


    override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()
        if (getItemInHand().isNull) {
            setItemInHand(Item.get(ItemID.BOW))
        }
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Skeleton"
    }

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get(Item.BONE), Item.get(Item.ARROW))
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun onUpdate(currentTick: Int): Boolean {
        burn(this)
        return super.onUpdate(currentTick)
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_SKELETON_SAY), RandomSoundEvaluator(), 5, 1),
                Behavior(
                    BowShootExecutor(
                        { this.getItemInHand() },
                        CoreMemoryTypes.Companion.ATTACK_TARGET,
                        0.3f,
                        15,
                        true,
                        30,
                        20
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET), 4, 1
                ),
                Behavior(
                    BowShootExecutor(
                        { this.getItemInHand() },
                        CoreMemoryTypes.Companion.NEAREST_GOLEM,
                        0.3f,
                        15,
                        true,
                        30,
                        20
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_GOLEM), 3, 1
                ),
                Behavior(
                    BowShootExecutor(
                        { this.getItemInHand() },
                        CoreMemoryTypes.Companion.NEAREST_PLAYER,
                        0.3f,
                        15,
                        true,
                        30,
                        20
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(16.0, 0.0, 20),
                NearestEntitySensor(EntityGolem::class.java, CoreMemoryTypes.Companion.NEAREST_GOLEM, 42.0, 0.0)
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }
}
