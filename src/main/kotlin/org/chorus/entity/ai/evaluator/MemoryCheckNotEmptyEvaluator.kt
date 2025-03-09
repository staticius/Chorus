package org.chorus.entity.ai.evaluator

import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.mob.EntityMob

class MemoryCheckNotEmptyEvaluator(protected var type: MemoryType<*>) : IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob): Boolean {
        return entity.behaviorGroup!!.memoryStorage!!.notEmpty(type)
    }
}
