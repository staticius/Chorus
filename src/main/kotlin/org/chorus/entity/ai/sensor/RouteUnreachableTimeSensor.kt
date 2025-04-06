package org.chorus.entity.ai.sensor

import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.mob.EntityMob

class RouteUnreachableTimeSensor(protected var type: MemoryType<Int>) : ISensor {
    override fun sense(entity: EntityMob) {
        val old = entity.memoryStorage[type]
        if (!entity.behaviorGroup.getRouteFinder()!!.isReachable) {
            entity.memoryStorage[type] = old + 1
        } else {
            entity.memoryStorage[type] = 0
        }
    }
}
