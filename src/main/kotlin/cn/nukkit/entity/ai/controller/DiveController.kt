package cn.nukkit.entity.ai.controller

import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.mob.EntityMob

/**
 * 下潜运动控制器，使实体下潜
 */
class DiveController : IController {
    override fun control(entity: EntityMob): Boolean {
        //add dive force
        if (entity.memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.ENABLE_DIVE_FORCE))  //                                                                  抵消额外的浮力即可
            entity.motion.y -= entity.gravity * (entity.floatingForceFactor - 1)
        return true
    }
}
