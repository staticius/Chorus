package org.chorus.entity.ai.evaluator

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.mob.EntityMob

class EntityCheckEvaluator(private val memoryType: MemoryType<out Entity?>?) :
    IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob): Boolean {
        if (entity.memoryStorage!!.isEmpty(memoryType)) {
            return false
        } else {
            val e = entity.memoryStorage!![memoryType]
            if (e is Player) {
                return (e.spawned && e.isOnline && (e.isSurvival || e.isAdventure) && e.isAlive())
            }
            return !e!!.isClosed
        }
    }
}
