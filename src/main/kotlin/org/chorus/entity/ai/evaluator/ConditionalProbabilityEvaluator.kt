package org.chorus.entity.ai.evaluator

import cn.nukkit.entity.Entity
import cn.nukkit.entity.mob.EntityMob
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Function

class ConditionalProbabilityEvaluator(
    probability1: Int,
    var probability2: Int,
    var condition: Function<Entity, Boolean>,
    total: Int
) :
    ProbabilityEvaluator(probability1, total) {
    override fun evaluate(entity: EntityMob): Boolean {
        return if (condition.apply(entity)) {
            ThreadLocalRandom.current().nextInt(total) < probability2
        } else ThreadLocalRandom.current().nextInt(total) < probability
    }
}
