package org.chorus.entity.ai.evaluator

import org.chorus.Player
import org.chorus.entity.Entity
import org.chorus.entity.ai.memory.IMemoryType
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.ai.memory.NullableMemoryType
import org.chorus.entity.mob.EntityMob

class EntityCheckEvaluator(private val memoryType: IMemoryType<out Entity?>) :
    IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob): Boolean {
        when (memoryType) {
            is MemoryType -> {
                val e = entity.memoryStorage[memoryType]!!
                if (e is Player) {
                    return (e.spawned && e.isOnline && (e.isSurvival || e.isAdventure) && e.isAlive())
                }
                return !e.isClosed()
            }

            is NullableMemoryType -> {
                if (entity.memoryStorage.isEmpty(memoryType)) {
                    return false
                } else {
                    val e = entity.memoryStorage[memoryType]!!
                    if (e is Player) {
                        return (e.spawned && e.isOnline && (e.isSurvival || e.isAdventure) && e.isAlive())
                    }
                    return !e.isClosed()
                }
            }
        }
    }
}
