package org.chorus.entity.ai.executor

import cn.nukkit.entity.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob

class WardenViolentAnimationExecutor(protected var duration: Int) : IBehaviorExecutor {
    protected var currentTick: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        currentTick++
        if (currentTick > duration) return false
        else {
            //更新视线target
            if (entity.memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET)) entity.lookTarget =
                entity.memoryStorage!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET).position
            return true
        }
    }

    override fun onInterrupt(entity: EntityMob) {
        this.currentTick = 0
        entity.setDataFlag(EntityFlag.ROARING, false)
        entity.setDataFlagExtend(EntityFlag.ROARING, false)
    }

    override fun onStart(entity: EntityMob) {
        entity.memoryStorage!!.put<Boolean>(CoreMemoryTypes.Companion.IS_ATTACK_TARGET_CHANGED, false)
        entity.moveTarget = null

        entity.setDataFlag(EntityFlag.ROARING, true)
        entity.setDataFlagExtend(EntityFlag.ROARING, true)
    }

    override fun onStop(entity: EntityMob) {
        this.currentTick = 0
        entity.setDataFlag(EntityFlag.ROARING, false)
        entity.setDataFlagExtend(EntityFlag.ROARING, false)
    }
}
