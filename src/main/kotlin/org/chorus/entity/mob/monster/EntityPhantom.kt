package org.chorus.entity.mob.monster

import org.chorus.Player
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.*
import org.chorus.entity.ai.executor.CircleAboveTargetExecutor
import org.chorus.entity.ai.executor.MeleeAttackExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.executor.SpaceRandomRoamExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.FlyingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus.entity.mob.EntityMob
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.*
import java.util.List
import java.util.Set
import java.util.function.Function

/**
 * @author PetteriM1
 */
class EntityPhantom(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityFlyable, EntitySmite {
    override fun getIdentifier(): String {
        return EntityID.Companion.PHANTOM
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_PHANTOM_IDLE, 0.8f, 1.2f, 0.8f, 0.8f),
                    all(RandomSoundEvaluator()),
                    6,
                    1
                ),
                Behavior(
                    CircleAboveTargetExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.4f, true), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_ENTITY),
                        not(PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, Utils.rand(200, 400)))
                    ), 5, 1
                ),
                Behavior(
                    CircleAboveTargetExecutor(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET, 0.4f, true),
                    all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_ENTITY),
                        not(PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, Utils.rand(200, 400)))
                    ),
                    4,
                    1
                ),
                Behavior(
                    PhantomMeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.5f, 64, false, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    3,
                    1
                ),
                Behavior(
                    PhantomMeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.5f,
                        64,
                        false,
                        30
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(
                    SpaceRandomRoamExecutor(0.15f, 12, 100, 20, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
                )
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(64.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 64.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    })
            ),
            Set.of<IController>(SpaceMoveController(), LookController(true, true), LiftController()),
            SimpleSpaceAStarRouteFinder(FlyingPosEvaluator(), this),
            this
        )
    }

    override fun isEnablePitch(): Boolean {
        return false
    }

    override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()
        this.diffHandDamage = floatArrayOf(4f, 6f, 9f)
    }

    override fun getWidth(): Float {
        return 0.9f
    }

    override fun getHeight(): Float {
        return 0.5f
    }

    override fun getOriginalName(): String {
        return "Phantom"
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(ItemID.PHANTOM_MEMBRANE))
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun onUpdate(currentTick: Int): Boolean {
        burn(this)
        return super.onUpdate(currentTick)
    }

    private inner class PhantomMeleeAttackExecutor(
        memory: MemoryType<out Entity?>?,
        speed: Float,
        maxSenseRange: Int,
        clearDataWhenLose: Boolean,
        coolDown: Int
    ) :
        MeleeAttackExecutor(memory, speed, maxSenseRange, clearDataWhenLose, coolDown, 2.5f) {
        override fun onStart(entity: EntityMob) {
            super.onStart(entity)
            entity.level!!.addSound(entity.position, Sound.MOB_PHANTOM_SWOOP)
        }
    }
}
