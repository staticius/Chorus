package cn.nukkit.entity.ai.executor

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.projectile.EntityBreezeWindCharge
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.event.entity.ProjectileLaunchEvent
import cn.nukkit.level.Sound
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.plugin.InternalPlugin
import java.util.concurrent.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class BreezeShootExecutor(
    protected var memory: MemoryType<out Entity?>,
    protected var speed: Float,
    maxShootDistance: Int,
    protected var clearDataWhenLose: Boolean,
    protected val coolDownTick: Int,
    protected val attackDelay: Int
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
                startShootSequence(entity)
            }
        } else if (tick2 != 0) {
            tick2++
            if (tick2 > attackDelay) {
                shootWindcharge(entity)
                entity.level!!.scheduler.scheduleDelayedTask(
                    InternalPlugin.INSTANCE,
                    { endShootSequence(entity) }, 20
                )
                tick2 = 0
                return target!!.health != 0f
            }
        }
        return true
    }

    override fun onStop(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        if (clearDataWhenLose) {
            entity.behaviorGroup!!.memoryStorage!!.clear(memory)
        }
        entity.isEnablePitch = false
        endShootSequence(entity)
        this.target = null
    }

    override fun onInterrupt(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        if (clearDataWhenLose) {
            entity.behaviorGroup!!.memoryStorage!!.clear(memory)
        }
        entity.isEnablePitch = false
        endShootSequence(entity)
        this.target = null
    }

    protected fun shootWindcharge(entity: EntityMob) {
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
            .putDouble("damage", 2.0)

        val p = 1.0
        val f = min((p * p + p * 2) / 3, 1.0) * 3

        val projectile: Entity = Entity.Companion.createEntity(
            EntityID.Companion.BREEZE_WIND_CHARGE_PROJECTILE,
            entity.level!!.getChunk(entity.position.chunkX, entity.position.chunkZ),
            nbt
        )
            ?: return

        if (projectile is EntityBreezeWindCharge) {
            projectile.shootingEntity = entity
        }

        val projectev = ProjectileLaunchEvent(projectile as EntityProjectile, entity)
        Server.getInstance().pluginManager.callEvent(projectev)
        if (projectev.isCancelled) {
            projectile.kill()
        } else {
            entity.level!!.addSound(entity.position, Sound.MOB_BREEZE_SHOOT)
            projectile.spawnToAll()
        }
    }

    private fun startShootSequence(entity: Entity) {
        entity.setDataProperty(EntityDataTypes.Companion.TARGET_EID, target!!.id)
        entity.level!!.addSound(entity.position, Sound.MOB_BREEZE_CHARGE)
    }

    private fun endShootSequence(entity: Entity) {
    }
}
