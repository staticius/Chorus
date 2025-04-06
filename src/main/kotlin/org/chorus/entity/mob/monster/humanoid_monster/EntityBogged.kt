package org.chorus.entity.mob.monster.humanoid_monster

import org.chorus.entity.EntityID
import org.chorus.entity.EntitySmite
import org.chorus.entity.EntityWalkable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.BowShootExecutor
import org.chorus.entity.ai.executor.FlatRandomRoamExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import java.util.Set

class EntityBogged(chunk: IChunk?, nbt: CompoundTag?) : EntitySkeleton(chunk, nbt), EntityWalkable, EntitySmite {
    override fun getIdentifier(): String {
        return EntityID.Companion.BOGGED
    }

    override fun initEntity() {
        this.maxHealth = 16
        super.initEntity()
        if (getItemInHand().isNull) {
            setItemInHand(Item.get(ItemID.BOW))
        }
    }

    override fun getOriginalName(): String {
        return "Bogged"
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(Item.BONE), Item.get(ItemID.ARROW)) //TODO: match vanilla drop
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_SKELETON_SAY), RandomSoundEvaluator(), 4, 1),
                Behavior(
                    BowShootExecutor(
                        { this.getItemInHand() },
                        CoreMemoryTypes.Companion.ATTACK_TARGET,
                        0.3f,
                        15,
                        true,
                        35,
                        20
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET), 3, 1
                ),
                Behavior(
                    BowShootExecutor(
                        { this.getItemInHand() },
                        CoreMemoryTypes.Companion.NEAREST_PLAYER,
                        0.3f,
                        15,
                        true,
                        35,
                        20
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(NearestPlayerSensor(16.0, 0.0, 20)),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }
}
