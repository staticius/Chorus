package org.chorus.entity.ai.executor

import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.animal.EntityAnimal

class AnimalGrowExecutor : IBehaviorExecutor {
    override fun execute(entity: EntityMob?): Boolean {
        if (entity is EntityAnimal) {
            entity.setBaby(false)
        }
        return false
    }
}
