package org.chorus.entity.ai.controller

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob

/**
 * 为飞行生物提供升力的运动控制器
 */
class LiftController : IController {
    override fun control(entity: EntityMob): Boolean {
        // add lift force
        if (entity.memoryStorage[CoreMemoryTypes.ENABLE_LIFT_FORCE]) entity.motion.y += entity.getGravity().toDouble()
        return true
    }
}
