package org.chorus.entity.ai.evaluator

import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.mob.EntityMob

class MemoryCheckEmptyEvaluator(protected var type: MemoryType<*>) : IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob): Boolean {
        return entity.getBehaviorGroup().getMemoryStorage().isEmpty(type)
    }
}
