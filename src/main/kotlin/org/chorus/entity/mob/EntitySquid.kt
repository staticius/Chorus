package org.chorus.entity.mob

import org.chorus.entity.EntityID
import org.chorus.entity.EntitySwimmable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.DiveController
import org.chorus.entity.ai.controller.LookController
import org.chorus.entity.ai.controller.SpaceMoveController
import org.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus.entity.ai.executor.SpaceRandomRoamExecutor
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.SwimmingPosEvaluator
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

open class EntitySquid(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntitySwimmable {
    override fun getEntityIdentifier(): String {
        return EntityID.SQUID
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf(),
            setOf<IBehavior>(
                Behavior(
                    SpaceRandomRoamExecutor(0.36f, 12, 1, 80, false, -1, false, 10),
                    IBehaviorEvaluator { entity: EntityMob? -> true }, 1
                )
            ),
            setOf(),
            setOf(SpaceMoveController(), LookController(true, true), DiveController()),
            SimpleSpaceAStarRouteFinder(SwimmingPosEvaluator(), this),
            this
        )
    }

    override fun getWidth(): Float {
        return 0.95f
    }

    override fun getHeight(): Float {
        return 0.95f
    }

    public override fun initEntity() {
        this.setMaxHealth(10)
        super.initEntity()
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(ItemID.INK_SAC, 0, 1))
    }

    override fun getOriginalName(): String {
        return "Squid"
    }
}
