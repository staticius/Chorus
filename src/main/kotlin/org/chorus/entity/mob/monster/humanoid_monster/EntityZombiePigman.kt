package org.chorus.entity.mob.monster.humanoid_monster

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntitySmite
import cn.nukkit.entity.EntityWalkable
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
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.MemorizedBlockSensor
import cn.nukkit.entity.ai.sensor.NearestEntitySensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityGolem
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import java.util.Set

/**
 * @author PikyCZ, Buddelbubi
 */
class EntityZombiePigman(chunk: IChunk?, nbt: CompoundTag?) : EntityZombie(chunk, nbt), EntityWalkable, EntitySmite {
    //Mojang seems to kept the old name?
    override fun getIdentifier(): String {
        return EntityID.Companion.ZOMBIE_PIGMAN
    }


    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
                Behavior(
                    NearestBlockIncementExecutor(),
                    { entity: EntityMob? ->
                        !memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.NEAREST_BLOCK) && memoryStorage!!.get<Block>(
                            CoreMemoryTypes.Companion.NEAREST_BLOCK
                        ) is BlockTurtleEgg
                    }, 1, 1
                )
            ),
            Set.of<IBehavior>(
                Behavior(
                    PlaySoundExecutor(
                        Sound.MOB_ZOMBIEPIG_ZPIG,
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
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    4,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_GOLEM, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_GOLEM),
                    3,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 40, false, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    2,
                    1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 0),
                NearestEntitySensor(EntityGolem::class.java, CoreMemoryTypes.Companion.NEAREST_GOLEM, 42.0, 0.0),
                MemorizedBlockSensor(11, 5, 20)
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun getFloatingForceFactor(): Double {
        return 0.0
    }

    override fun initEntity() {
        this.maxHealth = 20
        this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        super.initEntity()
        memoryStorage!!.put<Class<out Block>>(
            CoreMemoryTypes.Companion.LOOKING_BLOCK,
            BlockTurtleEgg::class.java
        )
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.9f
    }

    override fun getOriginalName(): String {
        return "Zombified Piglin"
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return this.getDataFlag(EntityFlag.ANGRY)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        burn(this)
        if (currentTick % 20 == 0) {
            EntityZombie.Companion.pickupItems(this)
        }
        return super.onUpdate(currentTick)
    }

    override fun getExperienceDrops(): Int {
        return if (isBaby()) 7 else 5
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source.cause == DamageCause.DROWNING) {
            return false
        }
        return super.attack(source)
    }
}
