package org.chorus.entity.mob.monster

import org.chorus.entity.EntityID
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.DistanceEvaluator
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.IBehaviorEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.mob.EntityMob
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.*
import java.util.Set

class EntityBreeze(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.BREEZE
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(
                Behavior(
                    BreezeShootExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.4f, 15, true, 30, 20),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    2,
                    1
                ),
                Behavior(
                    BreezeShootExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.4f, 15, true, 30, 20),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    2,
                    1
                )
            ),
            setOf<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_BREEZE_IDLE_AIR), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> !isOnGround() }), 7, 1
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_BREEZE_IDLE_GROUND), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> isOnGround() }), 6, 1
                ),
                Behavior(
                    BreezeJumpExecutor(), all(
                        any(
                            IBehaviorEvaluator { obj: EntityMob -> obj.isOnGround() },
                            IBehaviorEvaluator { obj: EntityMob -> obj.isInsideOfWater() }),
                        IBehaviorEvaluator { entity: EntityMob? -> getRiding() == null },
                        IBehaviorEvaluator { entity: EntityMob? -> !isInsideOfLava() }), 5, 1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 1.2f, true),
                    all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        DistanceEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET, 24.0)
                    ),
                    4,
                    1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 1.2f, true),
                    all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER, 24.0)
                    ),
                    3,
                    1
                ),
                Behavior(FlatRandomRoamExecutor(1f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            setOf<ISensor>(NearestPlayerSensor(24.0, 0.0, 20)),
            setOf<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 30
        super.initEntity()
    }

    override fun getHeight(): Float {
        return 1.77f
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(ItemID.BREEZE_ROD, 0, Utils.rand(1, 2)))
    }

    override fun getExperienceDrops(): Int {
        return 10
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source.cause == DamageCause.FALL) {
            return false
        }
        return super.attack(source)
    }
}
