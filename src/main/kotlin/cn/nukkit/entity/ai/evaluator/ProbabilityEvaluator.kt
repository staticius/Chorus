package cn.nukkit.entity.ai.evaluator

import cn.nukkit.entity.mob.EntityMob
import java.util.concurrent.ThreadLocalRandom

open class ProbabilityEvaluator(protected var probability: Int, protected var total: Int) : IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob?): Boolean {
        return ThreadLocalRandom.current().nextInt(total) < probability
    }
}
