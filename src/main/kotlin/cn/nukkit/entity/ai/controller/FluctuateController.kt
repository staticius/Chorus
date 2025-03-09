package cn.nukkit.entity.ai.controller

import cn.nukkit.entity.mob.EntityMob
import java.util.concurrent.ThreadLocalRandom

/**
 * 控制实体在水中扑腾的控制器
 */
class FluctuateController : IController {
    private var lastTickInWater = false

    override fun control(entity: EntityMob): Boolean {
        if (entity.hasWaterAt(entity.floatingHeight)) {
            if (!lastTickInWater) lastTickInWater = true
        } else {
            if (lastTickInWater) {
                lastTickInWater = false
                if (entity.hasWaterAt(0f)) {
                    if (ThreadLocalRandom.current().nextInt(0, 4) == 3) { // 1/3
                        entity.motion.y += entity.floatingHeight * 0.8
                    } else {
                        entity.motion.y += entity.floatingHeight * 0.6
                    }
                }
            }
        }
        return true
    }
}
