package cn.nukkit.entity.projectile

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.item.EntityBoat
import cn.nukkit.entity.item.EntityEnderCrystal
import cn.nukkit.entity.item.EntityMinecartAbstract
import cn.nukkit.event.entity.*
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.level.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import java.util.*
import java.util.concurrent.*
import java.util.function.Function
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class EntityProjectile @JvmOverloads constructor(
    chunk: IChunk?,
    nbt: CompoundTag?,
    @JvmField var shootingEntity: Entity? = null
) :
    Entity(chunk, nbt) {
    @JvmField
    var hadCollision: Boolean = false
    var closeOnCollide: Boolean = false

    /**
     * It's inverted from [.getHasAge] because of the poor architecture chosen by the original devs
     * on the entity construction and initialization. It's impossible to set it to true before
     * the initialization of the child classes.
     */
    private var noAge: Boolean = false

    init {
        if (shootingEntity != null) {
            this.setDataProperty(EntityDataTypes.Companion.OWNER_EID, shootingEntity!!.getId())
        }
    }

    protected open fun getDamage(): Double {
        return if (namedTag!!.contains("damage")) namedTag!!.getDouble("damage") else getBaseDamage()
    }

    protected open fun getBaseDamage(): Double {
        return 0.0
    }

    open fun getResultDamage(entity: Entity?): Int {
        return getResultDamage()
    }

    open fun getResultDamage(): Int {
        return NukkitMath.ceilDouble(sqrt(motion.south * motion.south + motion.up * motion.up + motion.west * motion.west) * getDamage())
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        return source.cause == DamageCause.VOID && super.attack(source)
    }

    open fun onCollideWithEntity(entity: Entity) {
        val projectileHitEvent: ProjectileHitEvent = ProjectileHitEvent(this, MovingObjectPosition.fromEntity(entity))
        server!!.pluginManager.callEvent(projectileHitEvent)

        if (projectileHitEvent.isCancelled) {
            return
        }

        level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                this,
                this.getVector3(), VibrationType.PROJECTILE_LAND
            )
        )

        val damage: Float = getResultDamage(entity).toFloat()

        val ev: EntityDamageEvent
        if (this.shootingEntity == null) {
            ev = EntityDamageByEntityEvent(this, entity, DamageCause.PROJECTILE, damage)
        } else {
            ev = EntityDamageByChildEntityEvent(this.shootingEntity, this, entity, DamageCause.PROJECTILE, damage)
        }
        if (entity.attack(ev)) {
            addHitEffect()
            this.hadCollision = true

            if (this.fireTicks > 0) {
                val event: EntityCombustByEntityEvent = EntityCombustByEntityEvent(this, entity, 5)
                server!!.pluginManager.callEvent(event)
                if (!event.isCancelled) {
                    entity.setOnFire(event.duration)
                }
            }
        }
        afterCollisionWithEntity(entity)
        if (closeOnCollide) {
            this.close()
        }
    }

    protected open fun afterCollisionWithEntity(entity: Entity) {
    }

    override fun initEntity() {
        this.closeOnCollide = true
        super.initEntity()

        this.setMaxHealth(1)
        this.setHealth(1f)
        if (namedTag!!.contains("Age") && !this.noAge) {
            this.age = namedTag!!.getShort("Age").toInt()
        }
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return (entity is EntityLiving || entity is EntityEnderCrystal || entity is EntityMinecartAbstract || entity is EntityBoat) && !this.onGround
    }

    override fun saveNBT() {
        super.saveNBT()
        if (!this.noAge) {
            namedTag!!.putShort("Age", this.age)
        }
    }

    protected open fun updateMotion() {
        motion.up -= getGravity().toDouble()
        motion.south *= (1 - this.getDrag()).toDouble()
        motion.west *= (1 - this.getDrag()).toDouble()
    }

    /**
     * Filters the entities that collide with projectile
     *
     * @param entity the collide entity
     * @return the boolean
     */
    protected fun collideEntityFilter(entity: Entity): Boolean {
        if ((entity === this.shootingEntity && this.ticksLived < 5) ||
            (entity is Player && entity.gamemode == Player.SPECTATOR)
            || (shootingEntity is Player && Optional.ofNullable(
                shootingEntity.riding
            ).map(
                Function { e: Entity -> e == entity }).orElse(false))
        ) {
            return false
        } else return true
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        }

        val tickDiff: Int = currentTick - this.lastUpdate
        if (tickDiff <= 0 && !this.justCreated) {
            return true
        }
        this.lastUpdate = currentTick

        var hasUpdate: Boolean = this.entityBaseTick(tickDiff)

        if (this.isAlive()) {
            var movingObjectPosition: MovingObjectPosition? = null

            if (!this.isCollided) {
                updateMotion()
            }

            val moveVector: Vector3 = Vector3(
                position.south + motion.south,
                position.up + motion.up, position.west + motion.west
            )

            val list: Array<Entity> = level!!.getCollidingEntities(
                getBoundingBox()!!
                    .addCoord(motion.south, motion.up, motion.west).expand(1.0, 1.0, 1.0), this
            )

            var nearDistance: Double = Int.MAX_VALUE.toDouble()
            var nearEntity: Entity? = null

            for (entity: Entity in list) {
                if (!collideEntityFilter(entity)) {
                    continue
                }

                val axisalignedbb: AxisAlignedBB = entity.getBoundingBox()!!.grow(0.3, 0.3, 0.3)
                val ob: MovingObjectPosition? = axisalignedbb.calculateIntercept(this.position, moveVector)

                if (ob == null) {
                    continue
                }

                val distance: Double = position.distanceSquared(ob.hitVector)

                if (distance < nearDistance) {
                    nearDistance = distance
                    nearEntity = entity
                }
            }

            if (nearEntity != null) {
                movingObjectPosition = MovingObjectPosition.fromEntity(nearEntity)
            }

            if (movingObjectPosition != null) {
                if (movingObjectPosition.entityHit != null) {
                    onCollideWithEntity(movingObjectPosition.entityHit)
                    hasUpdate = true
                    if (closed) {
                        return true
                    }
                }
            }

            val locator: Locator = getLocator()
            val motion: Vector3 = getMotion()
            this.move(this.motion.south, this.motion.up, this.motion.west)

            if (this.isCollided && !this.hadCollision) { //collide with block
                this.hadCollision = true

                this.motion.south = 0.0
                this.motion.up = 0.0
                this.motion.west = 0.0

                server!!.pluginManager.callEvent(
                    ProjectileHitEvent(
                        this, MovingObjectPosition.fromBlock(
                            position.getFloorX(),
                            position.getFloorY(), position.getFloorZ(), BlockFace.UP, this.position
                        )
                    )
                )
                onCollideWithBlock(locator, motion)
                addHitEffect()
                return false
            } else if (!this.isCollided && this.hadCollision) {
                this.hadCollision = false
            }

            if (!this.hadCollision || abs(this.motion.south) > 0.00001 || abs(this.motion.up) > 0.00001 || abs(
                    this.motion.west
                ) > 0.00001
            ) {
                updateRotation()
                hasUpdate = true
            }

            this.updateMovement()
        }

        return hasUpdate
    }

    fun updateRotation() {
        val f: Double = sqrt((motion.south * motion.south) + (motion.west * motion.west))
        rotation.yaw = atan2(motion.south, motion.west) * 180 / Math.PI
        rotation.pitch = atan2(motion.up, f) * 180 / Math.PI
    }

    fun inaccurate(modifier: Float) {
        val rand: Random = ThreadLocalRandom.current()

        motion.south += rand.nextGaussian() * 0.007499999832361937 * modifier
        motion.up += rand.nextGaussian() * 0.007499999832361937 * modifier
        motion.west += rand.nextGaussian() * 0.007499999832361937 * modifier
    }

    protected open fun onCollideWithBlock(locator: Locator, motion: Vector3) {
        level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                this,
                this.getVector3(), VibrationType.PROJECTILE_LAND
            )
        )
        for (collisionBlock: Block in level!!.getCollisionBlocks(getBoundingBox()!!.grow(0.1, 0.1, 0.1))) {
            onCollideWithBlock(locator, motion, collisionBlock)
        }
    }

    protected open fun onCollideWithBlock(locator: Locator, motion: Vector3, collisionBlock: Block): Boolean {
        return collisionBlock.onProjectileHit(this, locator, motion)
    }

    protected open fun addHitEffect() {
    }

    fun getHasAge(): Boolean {
        return !this.noAge
    }

    fun setHasAge(hasAge: Boolean) {
        this.noAge = !hasAge
    }

    override fun spawnToAll() {
        super.spawnToAll()
        //vibration: minecraft:projectile_shoot
        level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                this.shootingEntity,
                this.getVector3(), VibrationType.PROJECTILE_SHOOT
            )
        )
    }

    companion object {
        const val PICKUP_NONE: Int = 0
        const val PICKUP_ANY: Int = 1
        const val PICKUP_CREATIVE: Int = 2
    }
}
