package org.chorus.entity.mob.monster

import org.chorus.Player
import org.chorus.block.BlockSoulSand
import org.chorus.entity.EntityArthropod
import org.chorus.entity.EntityID
import org.chorus.entity.EntityWalkable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.FlatRandomRoamExecutor
import org.chorus.entity.ai.executor.MeleeAttackExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestEntitySensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.mob.EntityIronGolem
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import java.util.Set

/**
 * @author Box.
 */
class EntityEndermite(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityWalkable, EntityArthropod {
    override fun getIdentifier(): String {
        return EntityID.ENDERMITE
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            setOf<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_ENDERMITE_SAY), RandomSoundEvaluator(), 6, 1),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 16, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    4,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY, 0.3f, 16, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY),
                    3,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 16, false, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    2,
                    1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            setOf<ISensor>(
                NearestPlayerSensor(16.0, 0.0, 0),
                NearestEntitySensor(
                    EntityIronGolem::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    16.0,
                    0.0
                ),
                NearestEntitySensor(
                    EntityEnderman::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    16.0,
                    0.0
                )
            ),
            setOf<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 8
        this.diffHandDamage = floatArrayOf(2f, 2f, 3f)
        super.initEntity()
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (currentTick % 10 == 0) {
            if (this.locator.levelBlock.down() is BlockSoulSand) {
                this.attack(EntityDamageEvent(this, DamageCause.SUFFOCATION, 1f))
            }
        }
        return super.onUpdate(currentTick)
    }

    override fun getWidth(): Float {
        return 0.4f
    }

    override fun getHeight(): Float {
        return 0.3f
    }

    override fun getOriginalName(): String {
        return "Endermite"
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getExperienceDrops(): Int {
        return 3
    }
}
