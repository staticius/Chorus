package org.chorus.entity.ai.executor

import org.chorus.Server
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob
import org.chorus.network.protocol.EntityEventPacket

class InLoveExecutor(protected var duration: Int) : IBehaviorExecutor {
    protected var currentTick: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        if (currentTick == 0) {
            entity.memoryStorage!!.set<Int>(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, entity.level!!.tick)
            entity.memoryStorage!!.set<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE, true)
        }
        currentTick++
        if (currentTick > duration || !entity.memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE) /*interrupt by other*/) {
            currentTick = 0
            entity.memoryStorage!!.set<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE, false)
            return false
        }
        if (currentTick % 10 == 0) {
            sendLoveParticle(entity)
        }
        return true
    }

    override fun onInterrupt(entity: EntityMob) {
        entity.memoryStorage!!.set<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE, false)
        currentTick = 0
    }

    protected fun sendLoveParticle(entity: EntityMob) {
        val pk = EntityEventPacket()
        pk.eid = entity.id
        pk.event = EntityEventPacket.LOVE_PARTICLES
        Server.broadcastPacket(entity.viewers.values, pk)
    }
}
