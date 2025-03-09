package org.chorus.entity.mob.water_animal.fish

import org.chorus.entity.EntitySwimmable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.DiveController
import org.chorus.entity.ai.controller.LookController
import org.chorus.entity.ai.controller.SpaceMoveController
import org.chorus.entity.ai.executor.SpaceRandomRoamExecutor
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.SwimmingPosEvaluator
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.water_animal.EntityWaterAnimal
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import java.util.Set

/**
 * 所有鱼的基类
 */
abstract class EntityFish(chunk: IChunk?, nbt: CompoundTag) : EntityWaterAnimal(chunk, nbt), EntitySwimmable {
    //移除搁浅音效很不对味
    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf(),
            Set.of<IBehavior>(
                Behavior(
                    SpaceRandomRoamExecutor(0.36f, 12, 1, 80, false, -1, false, 10),
                    { entity: EntityMob? -> true }, 1
                )
            ),
            setOf(),
            Set.of(SpaceMoveController(), LookController(true, true), DiveController()),
            SimpleSpaceAStarRouteFinder(SwimmingPosEvaluator(), this),
            this
        )
    }
}
