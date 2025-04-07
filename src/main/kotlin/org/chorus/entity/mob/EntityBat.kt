package org.chorus.entity.mob

import org.chorus.entity.EntityFlyable
import org.chorus.entity.EntityID
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus.entity.ai.executor.SpaceRandomRoamExecutor
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.FlyingPosEvaluator
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import java.util.Set

/**
 * @author PikyCZ
 */
class EntityBat(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntityFlyable {
    override fun getIdentifier(): String {
        return EntityID.BAT
    }


    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf(),
            setOf<IBehavior>(
                Behavior(
                    SpaceRandomRoamExecutor(0.3f, 12, 100, 20, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
                )
            ),
            setOf(),
            setOf(SpaceMoveController(), LookController(true, true), LiftController()),
            SimpleSpaceAStarRouteFinder(FlyingPosEvaluator(), this),
            this
        )
    }

    override fun getWidth(): Float {
        return 0.5f
    }

    override fun getHeight(): Float {
        return 0.9f
    }

    public override fun initEntity() {
        this.maxHealth = 6
        super.initEntity()
    }

    override fun getOriginalName(): String {
        return "Bat"
    }

    override fun getExperienceDrops(): Int {
        return 0
    }
}
