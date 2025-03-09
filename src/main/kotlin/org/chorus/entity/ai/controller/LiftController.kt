package org.chorus.entity.ai.controller

import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.mob.EntityMob

/**
 * 为飞行生物提供升力的运动控制器
 */
class LiftController : IController {
    override fun control(entity: EntityMob): Boolean {
        //add lift force
        if (entity.memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.ENABLE_LIFT_FORCE)) entity.motion.y += entity.gravity.toDouble()
        return true
    }
}
