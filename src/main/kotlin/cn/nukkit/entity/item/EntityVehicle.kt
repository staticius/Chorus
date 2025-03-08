package cn.nukkit.entity.item

import cn.nukkit.Player
import cn.nukkit.entity.*
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.vehicle.VehicleDamageByEntityEvent
import cn.nukkit.event.vehicle.VehicleDamageEvent
import cn.nukkit.event.vehicle.VehicleDestroyByEntityEvent
import cn.nukkit.event.vehicle.VehicleDestroyEvent
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class EntityVehicle(chunk: IChunk?, nbt: CompoundTag?) : Entity(chunk, nbt), EntityRideable,
    EntityInteractable {
    protected var rollingDirection: Boolean = true

    fun getRollingAmplitude(): Int {
        return this.getDataProperty<Int>(EntityDataTypes.Companion.HURT_TICKS!!)
    }

    fun setRollingAmplitude(time: Int) {
        this.setDataProperty(EntityDataTypes.Companion.HURT_TICKS, time)
    }

    fun getRollingDirection(): Int {
        return this.getDataProperty<Int>(EntityDataTypes.Companion.HURT_DIRECTION!!)
    }

    fun setRollingDirection(direction: Int) {
        this.setDataProperty(EntityDataTypes.Companion.HURT_DIRECTION, direction)
    }

    fun getDamage(): Int {
        return this.getDataProperty<Int>(EntityDataTypes.Companion.STRUCTURAL_INTEGRITY!!) // false data name (should be DATA_DAMAGE_TAKEN)
    }

    fun setDamage(damage: Int) {
        this.setDataProperty(EntityDataTypes.Companion.STRUCTURAL_INTEGRITY, damage)
    }

    override fun getInteractButtonText(player: Player): String {
        return "action.interact.mount"
    }

    override fun canDoInteraction(): Boolean {
        return passengers.isEmpty()
    }

    override fun onUpdate(currentTick: Int): Boolean {
        // The rolling amplitude
        if (getRollingAmplitude() > 0) {
            setRollingAmplitude(getRollingAmplitude() - 1)
        }

        // A killer task
        if (this.level != null) {
            if (position.up < level.getMinHeight() - 16) {
                kill()
            }
        } else if (position.up < -16) {
            kill()
        }
        // Movement code
        updateMovement()

        //Check riding
        if (this.riding == null) {
            for (entity: Entity in level!!.fastNearbyEntities(
                boundingBox!!.grow(0.20000000298023224, 0.0, 0.20000000298023224),
                this
            )) {
                if (entity is EntityLiving) {
                    entity.collidingWith(this)
                }
            }
        }
        return true
    }

    protected fun performHurtAnimation(): Boolean {
        setRollingAmplitude(9)
        setRollingDirection(if (rollingDirection) 1 else -1)
        rollingDirection = !rollingDirection
        return true
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        var instantKill: Boolean = false

        if (source is EntityDamageByEntityEvent) {
            val damagingEntity: Entity = source.damager

            val byEvent: VehicleDamageByEntityEvent =
                VehicleDamageByEntityEvent(this, damagingEntity, source.getFinalDamage().toDouble())

            getServer()!!.pluginManager.callEvent(byEvent)

            if (byEvent.isCancelled) return false

            instantKill = damagingEntity is Player && damagingEntity.isCreative()
        } else {
            val damageEvent: VehicleDamageEvent = VehicleDamageEvent(this, source.getFinalDamage().toDouble())

            getServer()!!.pluginManager.callEvent(damageEvent)

            if (damageEvent.isCancelled) return false
        }

        if (instantKill || getHealth() - source.getFinalDamage() < 1) {
            if (source is EntityDamageByEntityEvent) {
                val damagingEntity: Entity = source.damager
                val byDestroyEvent: VehicleDestroyByEntityEvent = VehicleDestroyByEntityEvent(this, damagingEntity)

                getServer()!!.pluginManager.callEvent(byDestroyEvent)

                if (byDestroyEvent.isCancelled) return false
            } else {
                val destroyEvent: VehicleDestroyEvent = VehicleDestroyEvent(this)

                getServer()!!.pluginManager.callEvent(destroyEvent)

                if (destroyEvent.isCancelled) return false
            }
        }

        if (instantKill) source.setDamage(1000f)

        return super.attack(source)
    }
}
