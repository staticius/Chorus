package org.chorus.entity.ai.evaluator

import org.chorus.entity.mob.EntityMob


class NotMatchEvaluator(
    val evaluator: IBehaviorEvaluator
) : IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob): Boolean {
        return !evaluator.evaluate(entity)
    }
}
