package org.chorus.entity.ai.evaluator

import org.chorus.entity.mob.EntityMob
import lombok.AllArgsConstructor


class NotMatchEvaluator : IBehaviorEvaluator {
    private val evaluator: IBehaviorEvaluator? = null

    override fun evaluate(entity: EntityMob): Boolean {
        return !evaluator!!.evaluate(entity)
    }
}
