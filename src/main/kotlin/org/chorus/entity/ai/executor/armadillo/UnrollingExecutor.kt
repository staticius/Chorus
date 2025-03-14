package org.chorus.entity.ai.executor.armadillo

import org.chorus.entity.ai.executor.EntityControl
import org.chorus.entity.ai.executor.IBehaviorExecutor
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.animal.EntityArmadillo
import org.chorus.entity.mob.animal.EntityArmadillo.RollState

class UnrollingExecutor : EntityControl, IBehaviorExecutor {
    protected var tick: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        if (tick < STAY_TICKS) {
            tick++
            return true
        }
        return false
    }

    override fun onStart(entity: EntityMob?) {
        this.tick = 0
    }

    override fun onStop(entity: EntityMob) {
        if (entity is EntityArmadillo) {
            entity.rollState = RollState.UNROLLED
        }
    }

    override fun onInterrupt(entity: EntityMob) {
        onStop(entity)
    }

    companion object {
        private const val STAY_TICKS = 20
    }
}
