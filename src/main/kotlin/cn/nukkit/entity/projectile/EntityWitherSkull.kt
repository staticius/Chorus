package cn.nukkit.entity.projectile

import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.monster.EntityWither
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityExplosionPrimeEvent
import cn.nukkit.level.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag

open class EntityWitherSkull(chunk: IChunk?, nbt: CompoundTag?) : EntityProjectile(chunk, nbt), EntityExplosive {
    override fun getIdentifier(): String {
        return EntityID.Companion.WITHER_SKULL
    }

    override fun getDamage(): Double {
        return when (getServer()!!.getDifficulty()) {
            1 -> 5
            2 -> 8
            3 -> 12
            else -> 0
        }
    }

    override fun getWidth(): Float {
        return 0.25f
    }

    override fun getLength(): Float {
        return 0.25f
    }

    override fun getHeight(): Float {
        return 0.25f
    }

    protected open fun getStrength(): Float {
        return 1.2f
    }

    override fun onCollideWithEntity(entity: Entity) {
        if (entity !is EntityWither) {
            explode()
            entity.addEffect(Effect.Companion.get(EffectType.Companion.WITHER).setDuration(200))
            super.onCollideWithEntity(entity)
        }
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
        if (!affect && getLocator().getLevelBlock().isAir()) {
            explode()
        }
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        return false
    }

    override fun getOriginalName(): String {
        return "Wither Skull"
    }

    override fun explode() {
        if (this.closed) {
            return
        }
        this.close()
        val ev: EntityExplosionPrimeEvent = EntityExplosionPrimeEvent(this, getStrength().toDouble())
        ev.fireChance = 0.0
        server!!.pluginManager.callEvent(ev)
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
