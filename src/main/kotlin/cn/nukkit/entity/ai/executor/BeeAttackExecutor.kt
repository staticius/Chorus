package cn.nukkit.entity.ai.executor

import cn.nukkit.Server
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.animal.EntityBee

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
                    if (Server.getInstance().difficulty == 2) {
                        entity1.addEffect(Effect.Companion.get(EffectType.Companion.POISON).setDuration(200))
                    } else if (Server.getInstance().difficulty == 3) {
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
