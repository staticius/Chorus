package org.chorus.entity.ai.evaluator

import org.chorus.entity.ai.memory.NullableMemoryType
import org.chorus.entity.mob.EntityMob

class MemoryCheckEmptyEvaluator(private val type: NullableMemoryType<*>) : IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob): Boolean {
        return entity.behaviorGroup.memoryStorage.isEmpty(type)
    }
}
