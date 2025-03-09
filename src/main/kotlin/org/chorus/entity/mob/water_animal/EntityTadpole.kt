package org.chorus.entity.mob.water_animal

import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntitySwimmable
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.executor.SpaceRandomRoamExecutor
import cn.nukkit.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.SwimmingPosEvaluator
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import java.util.Set

class EntityTadpole(chunk: IChunk?, nbt: CompoundTag) : EntityWaterAnimal(chunk, nbt), EntitySwimmable {
    override fun getIdentifier(): String {
        return EntityID.Companion.TADPOLE
    }

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

    override fun getHeight(): Float {
        return 0.8f
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun initEntity() {
        this.maxHealth = 6
        super.initEntity()
    }

    override fun getOriginalName(): String {
        return "Tadpole"
    }
}
