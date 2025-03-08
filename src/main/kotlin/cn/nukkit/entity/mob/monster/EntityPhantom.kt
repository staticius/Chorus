package cn.nukkit.entity.mob.monster

import cn.nukkit.Player
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.*
import cn.nukkit.entity.ai.executor.CircleAboveTargetExecutor
import cn.nukkit.entity.ai.executor.MeleeAttackExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.executor.SpaceRandomRoamExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.FlyingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestTargetEntitySensor
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
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

    override fun getDrops(): Array<Item?> {
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
