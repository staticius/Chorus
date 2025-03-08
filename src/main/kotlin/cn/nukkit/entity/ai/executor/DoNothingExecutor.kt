package cn.nukkit.entity.ai.executor

import cn.nukkit.entity.mob.EntityMob

class DoNothingExecutor : IBehaviorExecutor {
    override fun execute(entity: EntityMob?): Boolean {
        return true
    }
}
