package org.chorus.entity.ai.executor

import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.math.IVector3

class WitherDashExecutor(
    memory: MemoryType<out IVector3?>?,
    speed: Float,
    updateRouteImmediatelyWhenTargetChange: Boolean,
    maxFollowRange: Float,
    minFollowRange: Float
) :
    MoveToTargetExecutor(memory, speed, updateRouteImmediatelyWhenTargetChange, maxFollowRange, minFollowRange, false) {
    protected var tick: Int = 0

    override fun onStart(entity: EntityMob) {
        super.onStart(entity)
        entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_DASH, entity.level!!.tick)
        entity.setDataFlag(EntityFlag.CAN_DASH)
    }

    override fun onStop(entity: EntityMob) {
        super.onStop(entity)
        entity.setDataFlag(EntityFlag.CAN_DASH, false)
    }

    override fun onInterrupt(entity: EntityMob) {
        super.onInterrupt(entity)
        entity.setDataFlag(EntityFlag.CAN_DASH, false)
    }
}
