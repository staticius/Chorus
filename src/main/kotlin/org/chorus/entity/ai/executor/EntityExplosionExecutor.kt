package org.chorus.entity.ai.executor

import org.chorus.Server
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.monster.EntityCreeper
import org.chorus.event.entity.EntityExplosionPrimeEvent
import org.chorus.level.Explosion
import org.chorus.level.GameRule
import org.chorus.level.Sound
import org.chorus.level.particle.HugeExplodeSeedParticle

class EntityExplosionExecutor @JvmOverloads constructor(
    protected var explodeTime: Int,
    protected var explodeForce: Int,
    protected var flagMemory: MemoryType<Boolean?>? = null
) :
    IBehaviorExecutor {
    protected var currentTick: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        //check flag
        if (flagMemory != null && entity.memoryStorage!!.compareDataTo(flagMemory, false)) {
            return false
        }

        currentTick++
        if (explodeTime > currentTick) {
            entity.level!!.addSound(entity.position, Sound.RANDOM_FUSE)
            entity.setDataProperty(EntityDataTypes.Companion.FUSE_TIME, currentTick)
            entity.setDataFlag(EntityFlag.IGNITED, true)
            return true
        } else {
            explode(entity)
            return false
        }
    }

    override fun onInterrupt(entity: EntityMob) {
        entity.setDataFlag(EntityFlag.IGNITED, false)
        currentTick = 0
    }

    override fun onStop(entity: EntityMob) {
        entity.setDataFlag(EntityFlag.IGNITED, false)
        currentTick = 0
    }

    protected fun explode(entity: EntityMob) {
        val ev = EntityExplosionPrimeEvent(
            entity,
            (if (entity is EntityCreeper) if (entity.isPowered) explodeForce * 2 else explodeForce else explodeForce).toDouble()
        )

        if (!entity.level!!.gameRules.getBoolean(GameRule.MOB_GRIEFING)) {
            ev.isBlockBreaking = false
        }

        Server.getInstance().pluginManager.callEvent(ev)

        if (!ev.isCancelled) {
            val explosion = Explosion(entity.locator, ev.force.toFloat().toDouble(), entity)

            if (ev.isBlockBreaking && entity.level!!.gameRules.getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA()
            }

            explosion.explodeB()
            entity.level!!.addParticle(HugeExplodeSeedParticle(entity.position))
        }

        entity.close()
    }
}
