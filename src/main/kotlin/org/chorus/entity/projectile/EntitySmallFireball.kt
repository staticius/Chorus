package org.chorus.entity.projectile

import org.chorus.Server
import org.chorus.block.Block
import org.chorus.block.BlockFire
import org.chorus.block.BlockID
import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.event.block.BlockIgniteEvent
import org.chorus.event.entity.EntityCombustByEntityEvent
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.event.entity.ProjectileHitEvent
import org.chorus.level.Locator
import org.chorus.level.MovingObjectPosition
import org.chorus.level.format.IChunk
import org.chorus.level.vibration.VibrationEvent
import org.chorus.level.vibration.VibrationType
import org.chorus.math.Vector3
import org.chorus.nbt.tag.CompoundTag

open class EntitySmallFireball(chunk: IChunk?, nbt: CompoundTag?) : EntityProjectile(chunk, nbt) {
    override fun getEntityIdentifier(): String {
        return EntityID.SMALL_FIREBALL
    }

    override fun getHeight(): Float {
        return 0.3125f
    }

    override fun getWidth(): Float {
        return 0.3125f
    }

    override fun getLength(): Float {
        return 0.3125f
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        }

        var hasUpdate: Boolean = super.onUpdate(currentTick)

        if (this.age > 1200 || this.isCollided) {
            this.kill()
            hasUpdate = true
        }

        return hasUpdate
    }

    override fun getResultDamage(): Int {
        return 6
    }

    override fun onCollideWithEntity(entity: Entity) {
        val projectileHitEvent: ProjectileHitEvent = ProjectileHitEvent(this, MovingObjectPosition.fromEntity(entity))
        Server.instance.pluginManager.callEvent(projectileHitEvent)
        if (projectileHitEvent.isCancelled) return
        level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                this,
                this.vector3, VibrationType.PROJECTILE_LAND
            )
        )
        val damage: Int = this.getResultDamage(entity)
        val ev: EntityDamageEvent = EntityDamageByEntityEvent(this, entity, DamageCause.PROJECTILE, damage.toFloat())
        if (entity.attack(ev)) {
            addHitEffect()
            this.hadCollision = true
            val event: EntityCombustByEntityEvent = EntityCombustByEntityEvent(this, entity, 5)
            Server.instance.pluginManager.callEvent(event)
            if (!event.isCancelled) entity.setOnFire(event.duration)
        }
        afterCollisionWithEntity(entity)
        this.close()
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
        if (!affect && getLocator().levelBlock.id === BlockID.AIR) {
            val fire: BlockFire = Block.get(BlockID.FIRE) as BlockFire
            fire.position.x = position.x
            fire.position.y = position.y
            fire.position.z = position.z
            fire.level = level!!

            if (fire.isBlockTopFacingSurfaceSolid(fire.down()) || fire.canNeighborBurn()) {
                val e = BlockIgniteEvent(
                    getLocator().levelBlock,
                    null,
                    null,
                    BlockIgniteEvent.BlockIgniteCause.FIREBALL
                )
                Server.instance.pluginManager.callEvent(e)
                if (!e.isCancelled) {
                    level!!.setBlock(fire.position, fire, true)
                }
            }
        }
    }

    override fun getOriginalName(): String {
        return "Small FireBall"
    }
}
