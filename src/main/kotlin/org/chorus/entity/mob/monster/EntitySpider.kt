package org.chorus.entity.mob.monster

import org.chorus.Player
import org.chorus.entity.Entity
import org.chorus.entity.EntityArthropod
import org.chorus.entity.EntityID
import org.chorus.entity.EntityWalkable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.IController
import org.chorus.entity.ai.controller.LookController
import org.chorus.entity.ai.controller.WalkController
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.FlatRandomRoamExecutor
import org.chorus.entity.ai.executor.FleeFromTargetExecutor
import org.chorus.entity.ai.executor.MeleeAttackExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestEntitySensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus.entity.mob.EntityGolem
import org.chorus.entity.mob.animal.EntityArmadillo
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import java.util.function.Function

class EntitySpider(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityWalkable, EntityArthropod {
    override fun getEntityIdentifier(): String {
        return EntityID.SPIDER
    }


    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            setOf<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_SPIDER_SAY), RandomSoundEvaluator(), 6, 1),
                Behavior(
                    FleeFromTargetExecutor(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY, 0.3f, true, 9f),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY),
                    5,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    4,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        40,
                        true,
                        30
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 3, 1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 40, false, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    2,
                    1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            setOf<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    listOf(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity -> this.attackTarget(entity) }),
                NearestEntitySensor(
                    EntityArmadillo::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    42.0,
                    0.0
                )
            ),
            setOf<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 16
        this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        super.initEntity()
    }

    override fun getWidth(): Float {
        return 1.4f
    }

    override fun getHeight(): Float {
        return 0.9f
    }

    override fun getOriginalName(): String {
        return "Spider"
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(ItemID.STRING), Item.get(ItemID.SPIDER_EYE))
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun attackTarget(entity: Entity): Boolean {
        return entity is EntityGolem
    }
}
