package org.chorus.entity.ai.executor

import org.chorus.entity.mob.EntityMob

class DoNothingExecutor : IBehaviorExecutor {
    override fun execute(entity: EntityMob): Boolean {
        return true
    }
}
