package org.chorus.entity.mob

import cn.nukkit.Player
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.MeleeAttackExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestTargetEntitySensor
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
import java.util.List
import java.util.Set
import java.util.function.Function

class EntityZoglin(chunk: IChunk?, nbt: CompoundTag?) : EntityMob(chunk, nbt!!), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.ZOGLIN
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    3,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    2,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        40,
                        true,
                        30
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity -> this.attackTarget(entity) })
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.setMaxHealth(40)
        super.initEntity()
        this.diffHandDamage = floatArrayOf(1f, 1f, 1f)
    }

    override fun getWidth(): Float {
        if (this.isBaby()) {
            return 0.85f
        }
        return 1.4f
    }

    override fun getHeight(): Float {
        if (this.isBaby()) {
            return 0.85f
        }
        return 1.4f
    }

    override fun getDiffHandDamage(): FloatArray? {
        if (isBaby()) {
            return super.getDiffHandDamage()
        } else return floatArrayOf(
            Utils.rand(2.5f, 5f),
            Utils.rand(3f, 8f),
            Utils.rand(4.5f, 12f),
        )
    }

    override fun getOriginalName(): String {
        return "Zoglin"
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getDrops(): Array<Item?> {
        return arrayOf(Item.get(Item.ROTTEN_FLESH, 0, Utils.rand(1, 3)))
    }

    override fun attackTarget(entity: Entity): Boolean {
        return (entity !is EntityZoglin && entity is EntityMob)
    }

    override fun getExperienceDrops(): Int {
        return if (isBaby()) 0 else Utils.rand(1, 3)
    }
}
