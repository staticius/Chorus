package org.chorus.entity.item

import org.chorus.entity.*
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.data.EntityFlag
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.event.entity.EntityExplosionPrimeEvent
import org.chorus.level.Explosion
import org.chorus.level.GameRule
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.level.vibration.VibrationEvent
import org.chorus.level.vibration.VibrationType
import org.chorus.nbt.tag.CompoundTag
import kotlin.math.abs

/**
 * @author MagicDroidX
 */
class EntityTnt @JvmOverloads constructor(chunk: IChunk?, nbt: CompoundTag?, protected var source: Entity? = null) :
    Entity(chunk, nbt), EntityExplosive {
    override fun getIdentifier(): String {
        return EntityID.TNT
    }

    protected var fuse: Int = 0

    override fun getWidth(): Float {
        return 0.98f
    }

    override fun getLength(): Float {
        return 0.98f
    }

    override fun getHeight(): Float {
        return 0.98f
    }

    override fun getGravity(): Float {
        return 0.04f
    }

    override fun getDrag(): Float {
        return 0.02f
    }

    override fun getBaseOffset(): Float {
        return 0.49f
    }

    override fun canCollide(): Boolean {
        return false
    }


    override fun attack(source: EntityDamageEvent): Boolean {
        return source.cause == DamageCause.VOID && super.attack(source)
    }

    override fun initEntity() {
        super.initEntity()

        if (namedTag!!.contains("Fuse")) {
            fuse = namedTag!!.getByte("Fuse").toInt()
        } else {
            fuse = 80
        }

        this.setDataFlag(EntityFlag.IGNITED, true)
        this.setDataProperty(EntityDataTypes.Companion.FUSE_TIME, fuse)

        level!!.addSound(this.position, Sound.RANDOM_FUSE)
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return false
    }

    override fun saveNBT() {
        super.saveNBT()
        namedTag!!.putByte("Fuse", fuse)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (closed) {
            return false
        }

        val tickDiff: Int = currentTick - lastUpdate

        if (tickDiff <= 0 && !justCreated) {
            return true
        }

        if (fuse % 5 == 0) {
            this.setDataProperty(EntityDataTypes.Companion.FUSE_TIME, fuse)
        }

        lastUpdate = currentTick

        val hasUpdate: Boolean = entityBaseTick(tickDiff)

        if (isAlive()) {
            motion.y -= getGravity().toDouble()

            move(motion.x, motion.y, motion.z)

            val friction: Float = 1 - getDrag()

            motion.x *= friction.toDouble()
            motion.y *= friction.toDouble()
            motion.z *= friction.toDouble()

            updateMovement()

            if (onGround) {
                motion.y *= -0.5
                motion.x *= 0.7
                motion.z *= 0.7
            }

            fuse -= tickDiff

            if (fuse <= 0) {
                if (level!!.gameRules.getBoolean(GameRule.TNT_EXPLODES)) {
                    explode()
                }
                kill()
            }
        }

        return hasUpdate || fuse >= 0 || abs(motion.x) > 0.00001 || abs(
            motion.y
        ) > 0.00001 || abs(motion.z) > 0.00001
    }

    override fun explode() {
        val event: EntityExplosionPrimeEvent = EntityExplosionPrimeEvent(this, 4.0)
        Server.instance.pluginManager.callEvent(event)
        if (event.isCancelled) {
            return
        }
        val explosion: Explosion = Explosion(this.getLocator(), event.force, this)
        explosion.fireChance = event.fireChance
        if (event.isBlockBreaking()) {
            explosion.explodeA()
        }
        explosion.explodeB()
        level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                this,
                this.vector3, VibrationType.EXPLODE
            )
        )
    }

    fun getSource(): Entity? {
        return source
    }

    override fun getOriginalName(): String {
        return "Block of TNT"
    }
}
