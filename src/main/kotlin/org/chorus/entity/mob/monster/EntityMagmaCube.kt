package org.chorus.entity.mob.monster

import org.chorus.block.*
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.EntityCheckEvaluator
import org.chorus.entity.ai.executor.FlatRandomRoamExecutor
import org.chorus.entity.ai.executor.MeleeAttackExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestTargetEntitySensor
import org.chorus.entity.mob.EntityGolem
import org.chorus.entity.mob.animal.EntityFrog
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.item.*
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.*
import java.util.List
import java.util.Set
import java.util.function.Function

class EntityMagmaCube(chunk: IChunk?, nbt: CompoundTag?) : EntityMonster(chunk, nbt!!), EntityWalkable, EntityVariant {
    override fun getIdentifier(): String {
        return EntityID.Companion.MAGMA_CUBE
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
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
            Set.of<ISensor>(
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity -> this.attackTarget(entity) })
            ),
            Set.of<IController>(HoppingController(40), LookController(true, true)),
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
            this.diffHandDamage = floatArrayOf(4f, 6f, 9f)
        } else if (getVariant() == SIZE_MEDIUM) {
            this.diffHandDamage = floatArrayOf(3f, 4f, 6f)
        } else {
            this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        }
        if (getVariant() == SIZE_BIG) {
            this.maxHealth = 16
        } else if (getVariant() == SIZE_MEDIUM) {
            this.maxHealth = 4
        } else if (getVariant() == SIZE_SMALL) {
            this.maxHealth = 1
        }
        setHealth(maxHealth.toFloat())
        recalculateBoundingBox()
    }

    override fun getFloatingForceFactor(): Double {
        return 0.0
    }

    override fun getWidth(): Float {
        if (getBehaviorGroup() == null) return 0f
        return 0.51f + getVariant() * 0.51f
    }

    override fun getHeight(): Float {
        if (getBehaviorGroup() == null) return 0f
        return 0.51f + getVariant() * 0.51f
    }


    override fun getFrostbiteInjury(): Int {
        return 5
    }

    override fun getOriginalName(): String {
        return "Magma Cube"
    }

    override fun getDrops(): Array<Item> {
        if (getLastDamageCause() != null) {
            if (lastDamageCause is EntityDamageByEntityEvent) {
                if (lastDamageCause.damager is EntityFrog) {
                    if (getVariant() == SIZE_SMALL) {
                        return arrayOf<Item?>(Item.get(if (frog.getVariant() == 0) Block.OCHRE_FROGLIGHT else if (frog.getVariant() == 1) Block.VERDANT_FROGLIGHT else Block.PEARLESCENT_FROGLIGHT))
                    }
                }
            }
        }
        if (getVariant() != SIZE_SMALL) {
            if (Utils.rand(0, 4) == 0) {
                return arrayOf(Item.get(Item.MAGMA_CREAM))
            }
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
                val magmaCube = EntityMagmaCube(this.locator.chunk, this.namedTag)
                magmaCube.setPosition(position.add(Utils.rand(-0.5, 0.5), 0.0, Utils.rand(-0.5, 0.5)))
                magmaCube.setRotation(rotation.yaw, rotation.pitch)
                magmaCube.setVariant(getSmaller())
                magmaCube.spawnToAll()
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
