package org.chorus.entity.ai.executor

import org.chorus.Player
import org.chorus.Server
import org.chorus.entity.*
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.entity.projectile.throwable.EntitySnowball
import org.chorus.event.entity.ProjectileLaunchEvent
import org.chorus.level.Sound
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.FloatTag
import org.chorus.nbt.tag.ListTag
import java.util.concurrent.*
import kotlin.math.cos
import kotlin.math.sin

class SnowGolemShootExecutor(
    protected var memory: MemoryType<out Entity?>,
    protected var speed: Float,
    maxShootDistance: Int,
    protected var clearDataWhenLose: Boolean,
    protected val coolDownTick: Int,
    protected val fireTick: Int
) :
    EntityControl, IBehaviorExecutor {
    protected var maxShootDistanceSquared: Int = maxShootDistance * maxShootDistance

    /**
     *
     *
     * Used to specify a specific attack target.
     */
    protected var target: Entity? = null

    private var tick1 = 0 //control the coolDownTick
    private var tick2 = 0 //control the pullBowTick

    override fun execute(entity: EntityMob): Boolean {
        if (tick2 == 0) {
            tick1++
        }
        if (!entity.isEnablePitch) entity.isEnablePitch = true
        if (entity.behaviorGroup!!.memoryStorage!!.isEmpty(memory)) return false
        val newTarget = entity.behaviorGroup!!.memoryStorage!![memory]
        if (this.target == null) target = newTarget

        if (!target!!.isAlive) return false
        else if (target is Player) {
            if (target.isCreative() || target.isSpectator() || !target.isOnline() || (entity.level!!.name != target.level.name)) {
                return false
            }
        }

        if (target!!.locator != newTarget!!.locator) {
            target = newTarget
        }

        if (entity.movementSpeed != speed) entity.movementSpeed = speed
        val clone = target!!.transform

        if (entity.position.distanceSquared(target!!.position) > maxShootDistanceSquared) {
            setRouteTarget(entity, clone.position)
        } else {
            setRouteTarget(entity, null)
        }
        setLookTarget(entity, clone.position)

        if (tick2 == 0 && tick1 > coolDownTick) {
            if (entity.position.distanceSquared(target!!.position) <= maxShootDistanceSquared) {
                this.tick1 = 0
                tick2++
            }
        } else if (tick2 != 0) {
            tick2++
            if (tick2 > fireTick) {
                shootSnowball(entity)
                tick2 = 0
                return target!!.health != 0f
            }
        }
        return true
    }

    override fun onStop(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        if (clearDataWhenLose) {
            entity.behaviorGroup!!.memoryStorage!!.clear(memory)
        }
        entity.isEnablePitch = false
        this.target = null
    }

    override fun onInterrupt(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        if (clearDataWhenLose) {
            entity.behaviorGroup!!.memoryStorage!!.clear(memory)
        }
        entity.isEnablePitch = false
        this.target = null
    }

    protected fun shootSnowball(entity: EntityMob) {
        val fireballTransform = entity.transform
        val directionVector =
            entity.directionVector.multiply((1 + ThreadLocalRandom.current().nextFloat(0.2f)).toDouble())
        fireballTransform.setY(entity.position.y + entity.eyeHeight + directionVector.getY())
        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(fireballTransform.position.x))
                    .add(FloatTag(fireballTransform.position.y))
                    .add(FloatTag(fireballTransform.position.z))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(-sin(entity.headYaw / 180 * Math.PI) * cos(entity.rotation.pitch / 180 * Math.PI)))
                    .add(FloatTag(-sin(entity.rotation.pitch / 180 * Math.PI)))
                    .add(FloatTag(cos(entity.headYaw / 180 * Math.PI) * cos(entity.rotation.pitch / 180 * Math.PI)))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag((if (entity.headYaw > 180) 360 else 0).toFloat() - entity.headYaw))
                    .add(FloatTag(-entity.rotation.pitch.toFloat()))
            )

        val projectile: Entity = Entity.Companion.createEntity(
            EntityID.Companion.SNOWBALL,
            entity.level!!.getChunk(entity.position.chunkX, entity.position.chunkZ),
            nbt
        )
            ?: return

        if (projectile is EntitySnowball) {
            projectile.shootingEntity = entity
        }

        val projectev = ProjectileLaunchEvent(projectile as EntityProjectile, entity)
        Server.getInstance().pluginManager.callEvent(projectev)
        if (projectev.isCancelled) {
            projectile.kill()
        } else {
            projectile.spawnToAll()
            entity.level!!.addSound(entity.position, Sound.MOB_SNOWGOLEM_SHOOT)
        }
    }
}
