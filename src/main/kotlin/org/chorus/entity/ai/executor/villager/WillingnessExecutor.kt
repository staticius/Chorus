package org.chorus.entity.ai.executor.villager

import org.chorus.entity.ai.executor.EntityControl
import org.chorus.entity.ai.executor.IBehaviorExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.villagers.EntityVillagerV2
import org.chorus.item.ItemFood

class WillingnessExecutor : EntityControl, IBehaviorExecutor {
    override fun execute(entity: EntityMob?): Boolean {
        return false
    }

    override fun onStart(entity: EntityMob) {
        if (entity is EntityVillagerV2) {
            for (j in 0..<entity.inventory.size) {
                val item = entity.inventory.getItem(j)
                if (item is ItemFood) {
                    entity.inventory.clear(j)
                }
            }
        }
        entity.memoryStorage!!.put<Boolean>(CoreMemoryTypes.Companion.WILLING, true)
    }
}
