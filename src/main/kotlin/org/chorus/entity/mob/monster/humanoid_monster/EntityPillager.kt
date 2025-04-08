package org.chorus.entity.mob.monster.humanoid_monster

import org.chorus.Player
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.CrossBowShootExecutor
import org.chorus.entity.ai.executor.FlatRandomRoamExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus.entity.mob.EntityGolem
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import java.util.List
import java.util.Set
import java.util.function.Function

class EntityPillager(chunk: IChunk?, nbt: CompoundTag?) : EntityIllager(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.PILLAGER
    }


    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            setOf<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_PILLAGER_IDLE, 0.8f, 1.2f, 0.8f, 0.8f),
                    RandomSoundEvaluator(),
                    5,
                    1
                ),
                Behavior(
                    CrossBowShootExecutor(
                        { this.itemInHand },
                        CoreMemoryTypes.Companion.ATTACK_TARGET,
                        0.3f,
                        15,
                        true,
                        30,
                        80
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET), 4, 1
                ),
                Behavior(
                    CrossBowShootExecutor(
                        { this.itemInHand },
                        CoreMemoryTypes.Companion.NEAREST_PLAYER,
                        0.3f,
                        15,
                        true,
                        30,
                        80
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER), 3, 1
                ),
                Behavior(
                    CrossBowShootExecutor(
                        { this.itemInHand },
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        15,
                        true,
                        30,
                        80
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            setOf<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    listOf(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    })
            ),
            setOf<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 24
        this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        super.initEntity()
        setItemInHand(Item.get(ItemID.CROSSBOW))
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Pillager"
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun attackTarget(entity: Entity): Boolean {
        return super.attackTarget(entity) || entity is EntityGolem
    }
}
