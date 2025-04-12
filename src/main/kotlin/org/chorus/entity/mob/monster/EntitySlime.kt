package org.chorus.entity.mob.monster

import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.entity.EntityVariant
import org.chorus.entity.EntityWalkable
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.HoppingController
import org.chorus.entity.ai.controller.IController
import org.chorus.entity.ai.controller.LookController
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.executor.FlatRandomRoamExecutor
import org.chorus.entity.ai.executor.MeleeAttackExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus.entity.mob.EntityGolem
import org.chorus.entity.mob.animal.EntityFrog
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.Utils
import java.util.function.Function

class EntitySlime(chunk: IChunk?, nbt: CompoundTag?) : EntityMonster(chunk, nbt!!), EntityWalkable, EntityVariant {
    override fun getEntityIdentifier(): String {
        return EntityID.SLIME
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            setOf<IBehavior>(
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    3,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        40,
                        false,
                        30
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            setOf<ISensor>(
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    listOf(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity -> this.attackTarget(entity) })
            ),
            setOf<IController>(HoppingController(40), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        super.initEntity()
        if (!hasVariant()) {
            this.setVariant(randomVariant())
        }

        if (getVariant() == SIZE_BIG) {
            this.diffHandDamage = floatArrayOf(3f, 4f, 6f)
        } else if (getVariant() == SIZE_MEDIUM) {
            this.diffHandDamage = floatArrayOf(2f, 2f, 3f)
        } else {
            this.diffHandDamage = floatArrayOf(0f, 0f, 0f)
        }

        if (getVariant() == SIZE_BIG) {
            this.maxHealth = 16
        } else if (getVariant() == SIZE_MEDIUM) {
            this.maxHealth = 4
        } else if (getVariant() == SIZE_SMALL) {
            this.maxHealth = 1
        }
        recalculateBoundingBox()
    }

    override fun getFloatingForceFactor(): Double {
        return 0.0
    }

    override fun getWidth(): Float {
        return 0.51f + getVariant() * 0.51f
    }

    override fun getHeight(): Float {
        return 0.51f + getVariant() * 0.51f
    }

    override fun getOriginalName(): String {
        return "Slime"
    }

    override fun getDrops(): Array<Item> {
        if (getVariant() == SIZE_SMALL) {
            if (getLastDamageCause() != null) {
                if (lastDamageCause is EntityDamageByEntityEvent) {
                    val damager = (lastDamageCause as EntityDamageByEntityEvent).damager
                    if (damager is EntityFrog) {
                        return arrayOf(Item.get(ItemID.SLIME_BALL))
                    }
                }
            }
            return arrayOf(Item.get(ItemID.SLIME_BALL, Utils.rand(0, 3)))
        }
        return Item.EMPTY_ARRAY
    }

    override fun getExperienceDrops(): Int {
        return getVariant()
    }

    override fun getAllVariant(): IntArray {
        return intArrayOf(1, 2, 4)
    }

    private fun getSmaller(): Int {
        return when (getVariant()) {
            4 -> 2
            else -> getVariant() - 1
        }
    }

    override fun kill() {
        if (getVariant() != SIZE_SMALL) {
            for (i in 1..<Utils.rand(2, 5)) {
                val slime = EntitySlime(this.locator.chunk, this.namedTag)
                slime.setPosition(position.add(Utils.rand(-0.5, 0.5), 0.0, Utils.rand(-0.5, 0.5)))
                slime.setRotation(rotation.yaw, rotation.pitch)
                slime.setVariant(getSmaller())
                slime.spawnToAll()
            }
        }
        super.kill()
    }

    override fun attackTarget(entity: Entity): Boolean {
        return super.attackTarget(entity) || entity is EntityGolem
    }

    companion object {
        const val SIZE_SMALL: Int = 1
        const val SIZE_MEDIUM: Int = 2
        const val SIZE_BIG: Int = 4
    }
}
