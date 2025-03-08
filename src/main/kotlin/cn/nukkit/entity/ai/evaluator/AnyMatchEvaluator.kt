package cn.nukkit.entity.ai.evaluator

import cn.nukkit.entity.mob.EntityMob

class AnyMatchEvaluator : MultiBehaviorEvaluator {
    constructor(evaluators: Set<IBehaviorEvaluator?>) : super(evaluators)

    constructor(vararg evaluators: IBehaviorEvaluator?) : super(*evaluators)

    override fun evaluate(entity: EntityMob): Boolean {
        for (evaluator in evaluators) {
            if (evaluator.evaluate(entity)) return true
        }
        return false
    }
}
