package org.chorus.entity.ai.executor.armadillo

import cn.nukkit.entity.ai.executor.EntityControl
import cn.nukkit.entity.ai.executor.IBehaviorExecutor
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.animal.EntityArmadillo
import cn.nukkit.entity.mob.animal.EntityArmadillo.RollState

class RelaxingExecutor : EntityControl, IBehaviorExecutor {
    override fun execute(entity: EntityMob?): Boolean {
        return false
    }

    override fun onStart(entity: EntityMob) {
        removeLookTarget(entity)
        removeRouteTarget(entity)
        if (entity is EntityArmadillo) {
            entity.rollState = RollState.ROLLED_UP_RELAXING
        }
    }
}
