package cn.nukkit.entity.ai.evaluator

import cn.nukkit.entity.mob.EntityMob
import lombok.AllArgsConstructor

@AllArgsConstructor
class NotMatchEvaluator : IBehaviorEvaluator {
    private val evaluator: IBehaviorEvaluator? = null

    override fun evaluate(entity: EntityMob): Boolean {
        return !evaluator!!.evaluate(entity)
    }
}
