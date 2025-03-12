package org.chorus.entity.projectile

import org.chorus.entity.Entity
import org.chorus.entity.EntityFlyable
import org.chorus.entity.EntityID
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.IController
import org.chorus.entity.ai.controller.LiftController
import org.chorus.entity.ai.controller.SpaceMoveController
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.executor.MoveToTargetExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.FlyingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.effect.Effect
import org.chorus.entity.effect.EffectType
import org.chorus.entity.mob.EntityMob
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import java.util.Set

class EntityShulkerBullet(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntityFlyable {
    override fun getIdentifier(): String {
        return EntityID.Companion.SHULKER_BULLET
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.2f, true),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    1,
                    1
                )
            ),
            setOf<ISensor>(),
            Set.of<IController>(SpaceMoveController(), LiftController()),
            SimpleSpaceAStarRouteFinder(FlyingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.setMaxHealth(1)
        super.initEntity()
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        kill()
        return true
    }

    override fun getWidth(): Float {
        return 0.3125f
    }

    override fun getHeight(): Float {
        return 0.3125f
    }

    override fun getOriginalName(): String {
        return "Shulker Bullet"
    }

    override fun onCollide(currentTick: Int, collidingEntities: List<Entity>): Boolean {
        close()
        for (entity: Entity in collidingEntities) {
            if (entity.attack(EntityDamageByEntityEvent(this, entity, DamageCause.CONTACT, 4f))) {
                level!!.addSound(entity.position, Sound.MOB_SHULKER_BULLET_HIT)
                entity.addEffect(Effect.get(EffectType.LEVITATION).setDuration(200))
            }
        }
        return true
    }

    override fun getExperienceDrops(): Int {
        return 0
    }
}
