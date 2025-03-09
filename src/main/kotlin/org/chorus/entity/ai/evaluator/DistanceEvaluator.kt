package org.chorus.entity.ai.evaluator

import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.mob.EntityMob
import org.chorus.math.IVector3

class DistanceEvaluator @JvmOverloads constructor(
    private val type: MemoryType<out IVector3?>?,
    private val maxDistance: Double,
    private val minDistance: Double = -1.0
) :
    IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob): Boolean {
        if (entity.memoryStorage!!.isEmpty(type)) {
            return false
        } else {
            val location = entity.memoryStorage!![type].vector3 ?: return false
            val distance = entity.position.distance(location)
            return distance <= maxDistance && distance >= minDistance
        }
    }
}
