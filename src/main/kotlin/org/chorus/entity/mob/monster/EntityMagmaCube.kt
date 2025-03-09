package org.chorus.entity.mob.monster

import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.MeleeAttackExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestTargetEntitySensor
import cn.nukkit.entity.mob.EntityGolem
import cn.nukkit.entity.mob.animal.EntityFrog
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
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

    override fun getDrops(): Array<Item?> {
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
