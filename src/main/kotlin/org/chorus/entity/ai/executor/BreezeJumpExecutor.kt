package org.chorus.entity.ai.executor

import org.chorus.Server
import org.chorus.entity.*
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.mob.EntityMob
import org.chorus.network.protocol.EntityEventPacket
import java.util.concurrent.*

class BreezeJumpExecutor : EntityControl, IBehaviorExecutor {
    private var prepareTick: Long = -1

    override fun execute(entity: EntityMob): Boolean {
        val tick = entity.level!!.tick.toLong()
        if (tick % 80 == 0L) {
            startSequence(entity)
            prepareTick = tick
        } else {
            if (prepareTick != -1L) {
                if (tick % 10 == 0L) {
                    prepareTick = -1
                    stopSequence(entity)
                }
            }
        }
        return true
    }

    override fun onStop(entity: EntityMob) {
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        entity.isEnablePitch = false
        stopSequence(entity)
    }

    override fun onInterrupt(entity: EntityMob) {
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        entity.isEnablePitch = false
        stopSequence(entity)
    }


    private fun startSequence(entity: Entity) {
        entity.setDataFlag(EntityFlag.JUMP_GOAL_JUMP)
    }

    private fun stopSequence(entity: Entity) {
        val random = ThreadLocalRandom.current()
        val motion = entity.directionVector
        motion!!.y = 0.6 + random.nextDouble(0.5)
        entity.setMotion(motion!!)
        entity.setDataFlag(EntityFlag.JUMP_GOAL_JUMP, false)
        val pk = EntityEventPacket()
        pk.eid = entity.id
        pk.event = EntityEventPacket.DUST_PARTICLES
        Server.broadcastPacket(entity.viewers.values, pk)
    }
}
