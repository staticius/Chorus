package org.chorus.entity.mob.monster

import org.chorus.Player
import org.chorus.entity.EntityFlyable
import org.chorus.entity.EntityID
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.DistanceEvaluator
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.evaluator.RandomSoundEvaluator
import org.chorus.entity.ai.executor.BlazeShootExecutor
import org.chorus.entity.ai.executor.MeleeAttackExecutor
import org.chorus.entity.ai.executor.PlaySoundExecutor
import org.chorus.entity.ai.executor.SpaceRandomRoamExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.FlyingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.*
import java.util.Set

/**
 * @author PikyCZ, Buddelbubi
 */
class EntityBlaze(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityFlyable {
    override fun getIdentifier(): String {
        return EntityID.BLAZE
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_BLAZE_BREATHE), RandomSoundEvaluator(), 5, 1),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 1, false, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER, 1.0)
                    ), 4, 1
                ),
                Behavior(
                    BlazeShootExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 15, true, 100, 40),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    3,
                    1
                ),
                Behavior(
                    BlazeShootExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 15, true, 100, 40),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    2,
                    1
                ),
                Behavior(SpaceRandomRoamExecutor(0.15f, 12, 100, 20, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(NearestPlayerSensor(40.0, 0.0, 20)),
            Set.of<IController>(SpaceMoveController(), LookController(true, true), LiftController()),
            SimpleSpaceAStarRouteFinder(FlyingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 20
        this.diffHandDamage = floatArrayOf(4f, 6f, 9f)
        super.initEntity()
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        val cause = source.cause
        if (cause == DamageCause.LAVA || cause == DamageCause.HOT_FLOOR || cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK) return false
        return super.attack(source)
    }

    override fun getWidth(): Float {
        return 0.5f
    }

    override fun getHeight(): Float {
        return 1.8f
    }

    override fun getOriginalName(): String {
        return "Blaze"
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getFrostbiteInjury(): Int {
        return 5
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(Item.get(Item.BLAZE_ROD, 0, Utils.rand(0, 1)))
    }

    override fun getExperienceDrops(): Int {
        return 10
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (currentTick % 10 == 0) {
            if (level!!.isRaining && !this.isUnderBlock) {
                this.attack(EntityDamageEvent(this, DamageCause.WEATHER, 1f))
            }
        }
        return super.onUpdate(currentTick)
    }
}
