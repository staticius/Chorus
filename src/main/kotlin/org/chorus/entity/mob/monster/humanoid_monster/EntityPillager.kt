package org.chorus.entity.mob.monster.humanoid_monster

import cn.nukkit.Player
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.CrossBowShootExecutor
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestTargetEntitySensor
import cn.nukkit.entity.mob.EntityGolem
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import java.util.List
import java.util.Set
import java.util.function.Function

class EntityPillager(chunk: IChunk?, nbt: CompoundTag?) : EntityIllager(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.PILLAGER
    }


    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_PILLAGER_IDLE, 0.8f, 1.2f, 0.8f, 0.8f),
                    RandomSoundEvaluator(),
                    5,
                    1
                ),
                Behavior(
                    CrossBowShootExecutor(
                        { this.getItemInHand() },
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
                        { this.getItemInHand() },
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
                        { this.getItemInHand() },
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
            Set.of<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    })
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 24
        this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        super.initEntity()
        setItemInHand(Item.get(Item.CROSSBOW))
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
