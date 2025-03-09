package org.chorus.entity.ai.executor

import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.animal.EntityAnimal

class AnimalGrowExecutor : IBehaviorExecutor {
    override fun execute(entity: EntityMob): Boolean {
        if (entity is EntityAnimal) {
            entity.setBaby(false)
        }
        return false
    }
}
