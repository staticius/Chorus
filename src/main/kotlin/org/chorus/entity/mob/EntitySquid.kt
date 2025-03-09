package org.chorus.entity.mob

import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntitySwimmable
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.executor.SpaceRandomRoamExecutor
import cn.nukkit.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.SwimmingPosEvaluator
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import java.util.Set

/**
 * @author PikyCZ
 */
open class EntitySquid(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntitySwimmable {
    override fun getIdentifier(): String {
        return EntityID.Companion.SQUID
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf(),
            Set.of<IBehavior>(
                Behavior(
                    SpaceRandomRoamExecutor(0.36f, 12, 1, 80, false, -1, false, 10),
                    IBehaviorEvaluator { entity: EntityMob? -> true }, 1
                )
            ),
            setOf(),
            Set.of(SpaceMoveController(), LookController(true, true), DiveController()),
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

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get(ItemID.INK_SAC, 0, 1))
    }

    override fun getOriginalName(): String {
        return "Squid"
    }
}
