package org.chorus.entity.ai.executor.villager

import cn.nukkit.entity.ai.executor.EntityControl
import cn.nukkit.entity.ai.executor.IBehaviorExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.villagers.EntityVillagerV2
import cn.nukkit.item.ItemFood

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
