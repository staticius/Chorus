package cn.nukkit.entity.mob.monster

import cn.nukkit.Player
import cn.nukkit.entity.EntityFlyable
import cn.nukkit.entity.EntityID
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.DistanceEvaluator
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.BlazeShootExecutor
import cn.nukkit.entity.ai.executor.MeleeAttackExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.executor.SpaceRandomRoamExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.FlyingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
import java.util.Set

/**
 * @author PikyCZ, Buddelbubi
 */
class EntityBlaze(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityFlyable {
    override fun getIdentifier(): String {
        return EntityID.Companion.BLAZE
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

    override fun getDrops(): Array<Item?> {
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
