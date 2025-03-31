package org.chorus.entity.ai.evaluator

import org.chorus.entity.mob.EntityMob

class RandomSoundEvaluator @JvmOverloads constructor(ticks: Int = 20, probability: Int = 10) :
    AllMatchEvaluator(
        setOf(
            IBehaviorEvaluator { entity: EntityMob -> entity.level!!.tick % ticks == 0 },
            ProbabilityEvaluator(1, probability)
        )
    )
