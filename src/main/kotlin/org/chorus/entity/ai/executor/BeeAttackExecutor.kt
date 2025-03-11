package org.chorus.entity.ai.executor

import org.chorus.Server
import org.chorus.entity.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.effect.*
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.animal.EntityBee

class BeeAttackExecutor(
    memory: MemoryType<out Entity?>?,
    speed: Float,
    maxSenseRange: Int,
    clearDataWhenLose: Boolean,
    coolDown: Int
) :
    MeleeAttackExecutor(memory, speed, maxSenseRange, clearDataWhenLose, coolDown) {
    override fun execute(entity: EntityMob): Boolean {
        if (entity is EntityBee) {
            if (entity.getMemoryStorage()!!.notEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET)) {
                if (!entity.isEnablePitch()) entity.setEnablePitch(true)
                val entity1 = entity.getMemoryStorage()!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET)
                if (entity1 != null) {
                    this.lookTarget = entity1.position.clone()
                    if (Server.instance.difficulty == 2) {
                        entity1.addEffect(Effect.Companion.get(EffectType.Companion.POISON).setDuration(200))
                    } else if (Server.instance.difficulty == 3) {
                        entity1.addEffect(Effect.Companion.get(EffectType.Companion.POISON).setDuration(360))
                    }
                }
            }

            if (entity.position.distanceSquared(
                    entity.getMemoryStorage()!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET).position
                ) <= 2.5 && attackTick > coolDown && entity.hasSting()
            ) {
                entity.dieInTicks = 700
            }
            return super.execute(entity)
        }
        return false
    }

    override fun onStop(entity: EntityMob) {
        stop(entity)
        super.onStop(entity)
    }

    override fun onInterrupt(entity: EntityMob) {
        stop(entity)
        super.onInterrupt(entity)
    }

    private fun stop(entity: EntityMob) {
        val bee = entity as EntityBee
        entity.level!!.scheduler.scheduleDelayedTask(null, { bee.setAngry(false) }, 5)
    }
}
