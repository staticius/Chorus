package cn.nukkit.entity.ai.evaluator

import cn.nukkit.entity.mob.EntityMob
import java.util.Set

class RandomSoundEvaluator @JvmOverloads constructor(ticks: Int = 20, probability: Int = 10) :
    AllMatchEvaluator(
        Set.of(
            IBehaviorEvaluator { entity: EntityMob -> entity.level!!.tick % ticks == 0 },
            ProbabilityEvaluator(1, probability)
        )
    )
