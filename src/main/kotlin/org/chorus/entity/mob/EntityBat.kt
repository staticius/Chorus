package org.chorus.entity.mob

import cn.nukkit.entity.EntityFlyable
import cn.nukkit.entity.EntityID
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.executor.SpaceRandomRoamExecutor
import cn.nukkit.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.FlyingPosEvaluator
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import java.util.Set

/**
 * @author PikyCZ
 */
class EntityBat(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntityFlyable {
    override fun getIdentifier(): String {
        return EntityID.Companion.BAT
    }


    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf(),
            Set.of<IBehavior>(
                Behavior(
                    SpaceRandomRoamExecutor(0.3f, 12, 100, 20, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
                )
            ),
            setOf(),
            Set.of(SpaceMoveController(), LookController(true, true), LiftController()),
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
