package org.chorus.entity.mob.animal

import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntitySwimmable
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
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.LookAtTargetExecutor
import cn.nukkit.entity.ai.executor.MoveToTargetExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestFeedingPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import java.util.Set

/**
 * @author PetteriM1
 */
class EntityTurtle(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntitySwimmable, EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.TURTLE
    }


    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    FlatRandomRoamExecutor(0.4f, 12, 40, true, 100, true, 10),
                    PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 100),
                    4,
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
            Set.of<IController>(
                WalkController(),
                LookController(true, true),
                FluctuateController(),
                SpaceMoveController(),
                DiveController()
            ),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun getOriginalName(): String {
        return "Turtle"
    }

    override fun getWidth(): Float {
        if (this.isBaby()) {
            return 0.6f
        }
        return 1.2f
    }

    override fun getHeight(): Float {
        if (this.isBaby()) {
            return 0.2f
        }
        return 0.4f
    }

    public override fun initEntity() {
        this.maxHealth = 30
        super.initEntity()
    }

    fun setBreedingAge(ticks: Int) {
    }

    fun setHomePos(pos: Vector3?) {
    }
}
