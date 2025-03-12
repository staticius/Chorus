package org.chorus.entity.ai.sensor

import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.mob.EntityMob

class RouteUnreachableTimeSensor(protected var type: MemoryType<Int?>) : ISensor {
    override fun sense(entity: EntityMob) {
        val old = entity.memoryStorage!!.get(type)
        if (!entity.behaviorGroup!!.routeFinder.isReachable) {
            entity.memoryStorage!!.put(type, old + 1)
        } else {
            entity.memoryStorage!!.put(type, 0)
        }
    }
}
