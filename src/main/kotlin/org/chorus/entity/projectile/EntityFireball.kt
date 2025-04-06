package org.chorus.entity.projectile

import org.chorus.block.*
import org.chorus.entity.*
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityExplosionPrimeEvent
import org.chorus.level.*
import org.chorus.level.format.IChunk
import org.chorus.level.vibration.VibrationEvent
import org.chorus.level.vibration.VibrationType
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag

class EntityFireball(chunk: IChunk?, nbt: CompoundTag?) : EntitySmallFireball(chunk, nbt), EntityExplosive {
    var directionChanged: Entity? = null

    override fun getIdentifier(): String {
        return EntityID.FIREBALL
    }

    override fun getWidth(): Float {
        return 0.31f
    }

    override fun getHeight(): Float {
        return 0.31f
    }

    override fun getLength(): Float {
        return 0.31f
    }

    override fun onCollideWithEntity(entity: Entity) {
        if (directionChanged != null) {
            if (directionChanged === entity) return
        }
        explode()
        super.onCollideWithEntity(entity)
    }

    override fun onCollideWithBlock(locator: Locator, motion: Vector3) {
        level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                this,
                this.vector3, VibrationType.PROJECTILE_LAND
            )
        )
        var affect: Boolean = false
        for (collisionBlock: Block in level!!.getCollisionBlocks(getBoundingBox().grow(0.1, 0.1, 0.1))) affect =
            onCollideWithBlock(locator, motion, collisionBlock)
        if (!affect && getLocator().getLevelBlock().isAir()) {
            explode()
        }
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (this.directionChanged == null && source is EntityDamageByEntityEvent) {
            this.directionChanged = source.damager
            this.setMotion(source.damager.getDirectionVector())
        }
        return true
    }

    override fun getOriginalName(): String {
        return "FireBall"
    }

    override fun explode() {
        if (this.closed) {
            return
        }
        this.close()
        val ev: EntityExplosionPrimeEvent = EntityExplosionPrimeEvent(this, 1.2)
        ev.fireChance = .4
        Server.instance.pluginManager.callEvent(ev)
        if (!ev.isCancelled) {
            val explosion: Explosion =
                Explosion(this.getLocator(), ev.force.toFloat().toDouble(), this.shootingEntity)
            explosion.fireChance = ev.fireChance
            if (ev.isBlockBreaking() && level!!.gameRules.getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA()
            }
            explosion.explodeB()
        }
    }
}
