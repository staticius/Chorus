package org.chorus.entity.ai.evaluator

import org.chorus.entity.mob.EntityMob
import org.chorus.math.Vector3

class BlockCheckEvaluator(protected var BlockID.String, protected var offsetVec: Vector3) : IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob): Boolean {
        return entity.level!!.getTickCachedBlock(entity.position.add(offsetVec)).id == blockId
    }
}
