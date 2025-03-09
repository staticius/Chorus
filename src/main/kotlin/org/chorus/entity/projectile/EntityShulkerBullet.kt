package org.chorus.entity.projectile

import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.IController
import cn.nukkit.entity.ai.controller.LiftController
import cn.nukkit.entity.ai.controller.SpaceMoveController
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.executor.MoveToTargetExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.FlyingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
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

    override fun onUpdate(currentTick: Int): Boolean {
        return super.onUpdate(currentTick)
    }

    override fun onCollide(currentTick: Int, collidingEntities: List<Entity>): Boolean {
        close()
        for (entity: Entity in collidingEntities) {
            if (entity.attack(EntityDamageByEntityEvent(this, entity, DamageCause.CONTACT, 4f))) {
                level!!.addSound(entity.position, Sound.MOB_SHULKER_BULLET_HIT)
                entity.addEffect(Effect.Companion.get(EffectType.Companion.LEVITATION).setDuration(200))
            }
        }
        return true
    }

    override fun getExperienceDrops(): Int {
        return 0
    }
}
