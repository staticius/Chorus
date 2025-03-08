package cn.nukkit.entity.mob.monster

import cn.nukkit.entity.EntityID
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.DistanceEvaluator
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
import java.util.Set

class EntityBreeze(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.BREEZE
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
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
            Set.of<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_BREEZE_IDLE_AIR), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> !isOnGround }), 7, 1
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_BREEZE_IDLE_GROUND), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> isOnGround }), 6, 1
                ),
                Behavior(
                    BreezeJumpExecutor(), all(
                        any(
                            IBehaviorEvaluator { obj: EntityMob -> obj.isOnGround },
                            IBehaviorEvaluator { obj: EntityMob -> obj.isInsideOfWater }),
                        IBehaviorEvaluator { entity: EntityMob? -> getRiding() == null },
                        IBehaviorEvaluator { entity: EntityMob? -> !isInsideOfLava }), 5, 1
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
            Set.of<ISensor>(NearestPlayerSensor(24.0, 0.0, 20)),
            Set.of<IController>(WalkController(), LookController(true, true)),
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

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get(Item.BREEZE_ROD, 0, Utils.rand(1, 2)))
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
