package org.chorus.entity.mob.monster.humanoid_monster

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.BlockSensor
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestTargetEntitySensor
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.LevelSoundEventPacket
import java.util.List
import java.util.Set
import java.util.function.Function

/**
 * @author PikyCZ
 */
class EntityHusk(chunk: IChunk?, nbt: CompoundTag?) : EntityZombie(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.HUSK
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    PlaySoundExecutor(
                        Sound.MOB_HUSK_AMBIENT,
                        if (isBaby()) 1.3f else 0.8f,
                        if (isBaby()) 1.7f else 1.2f,
                        1f,
                        1f
                    ), RandomSoundEvaluator(), 7, 1
                ),
                Behavior(
                    JumpExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob? -> !memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.NEAREST_BLOCK) },
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.getCollisionBlocks()!!.stream().anyMatch { block: Block? -> block is BlockTurtleEgg }
                        }), 6, 1, 10
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_BLOCK, 0.3f, true),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK),
                    5,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 10, Effect.Companion.get(
                            EffectType.Companion.HUNGER
                        ).setDuration(140)
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET), 4, 1
                ),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 40, false, 10, Effect.Companion.get(
                            EffectType.Companion.HUNGER
                        ).setDuration(140)
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER), 3, 1
                ),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        40,
                        true,
                        30,
                        Effect.Companion.get(
                            EffectType.Companion.HUNGER
                        ).setDuration(140)
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
                    }),
                BlockSensor(BlockTurtleEgg::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 11, 15, 10)

            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 20
        this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        super.initEntity()
        this.setDataProperty(EntityDataTypes.Companion.AMBIENT_SOUND_INTERVAL, 8)
        this.setDataProperty(EntityDataTypes.Companion.AMBIENT_SOUND_INTERVAL_RANGE, 16)
        this.setDataProperty(EntityDataTypes.Companion.AMBIENT_SOUND_EVENT_NAME, LevelSoundEventPacket.SOUND_AMBIENT)
        if (this.isBaby()) {
            this.setDataProperty(
                EntityDataTypes.Companion.AMBIENT_SOUND_EVENT_NAME,
                LevelSoundEventPacket.SOUND_AMBIENT_BABY
            )
        }
    }

    override fun getWidth(): Float {
        return if (this.isBaby()) 0.3f else 0.6f
    }

    override fun getHeight(): Float {
        return if (this.isBaby()) 0.95f else 1.9f
    }

    override fun getOriginalName(): String {
        return "Husk"
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getFloatingForceFactor(): Double {
        return 0.0
    }

    override fun transform() {
        this.close()
        val drowned = EntityZombie(this.locator.chunk, this.namedTag)
        drowned.setPosition(this.position)
        drowned.setRotation(rotation.yaw, rotation.pitch)
        drowned.spawnToAll()
        drowned.level!!.addSound(drowned.position, Sound.MOB_HUSK_CONVERT_TO_ZOMBIE)
    }
}
