package cn.nukkit.entity.projectile

import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.event.block.BlockIgniteEvent
import cn.nukkit.event.entity.EntityCombustByEntityEvent
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.event.entity.ProjectileHitEvent
import cn.nukkit.level.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag

open class EntitySmallFireball(chunk: IChunk?, nbt: CompoundTag?) : EntityProjectile(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.SMALL_FIREBALL
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
        server!!.pluginManager.callEvent(projectileHitEvent)
        if (projectileHitEvent.isCancelled) return
        level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                this,
                this.getVector3(), VibrationType.PROJECTILE_LAND
            )
        )
        val damage: Int = this.getResultDamage(entity)
        val ev: EntityDamageEvent = EntityDamageByEntityEvent(this, entity, DamageCause.PROJECTILE, damage.toFloat())
        if (entity.attack(ev)) {
            addHitEffect()
            this.hadCollision = true
            val event: EntityCombustByEntityEvent = EntityCombustByEntityEvent(this, entity, 5)
            server!!.pluginManager.callEvent(event)
            if (!event.isCancelled) entity.setOnFire(event.duration)
        }
        afterCollisionWithEntity(entity)
        this.close()
    }

    override fun onCollideWithBlock(locator: Locator, motion: Vector3) {
        level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                this,
                this.getVector3(), VibrationType.PROJECTILE_LAND
            )
        )
        var affect: Boolean = false
        for (collisionBlock: Block in level!!.getCollisionBlocks(getBoundingBox()!!.grow(0.1, 0.1, 0.1))) affect =
            onCollideWithBlock(locator, motion, collisionBlock)
        if (!affect && getLocator().getLevelBlock().getId() === BlockID.AIR) {
            val fire: BlockFire = Block.get(BlockID.FIRE) as BlockFire
            fire.position.x = position.x
            fire.position.y = position.y
            fire.position.z = position.z
            fire.level = level!!

            if (fire.isBlockTopFacingSurfaceSolid(fire.down()) || fire.canNeighborBurn()) {
                val e: BlockIgniteEvent = BlockIgniteEvent(
                    getLocator().getLevelBlock(),
                    null,
                    null,
                    BlockIgniteEvent.BlockIgniteCause.FIREBALL
                )
                level!!.server.pluginManager.callEvent(e)
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
