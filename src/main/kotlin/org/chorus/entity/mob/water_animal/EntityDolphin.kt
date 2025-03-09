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
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import java.util.Set

/**
 * @author PetteriM1
 */
class EntityDolphin(chunk: IChunk?, nbt: CompoundTag) : EntityWaterAnimal(chunk, nbt), EntitySwimmable {
    override fun getIdentifier(): String {
        return EntityID.Companion.DOLPHIN
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


    override fun getOriginalName(): String {
        return "Dolphin"
    }

    override fun getWidth(): Float {
        return 0.9f
    }

    override fun getHeight(): Float {
        return 0.6f
    }

    public override fun initEntity() {
        this.maxHealth = 10
        super.initEntity()
    }

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get(Item.COD))
    }

    override fun getExperienceDrops(): Int {
        return 0
    }
}
