package org.chorus.entity.ai.executor.villager

import org.chorus.block.*
import org.chorus.entity.ai.executor.EntityControl
import org.chorus.entity.ai.executor.IBehaviorExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob

class DoorExecutor : EntityControl, IBehaviorExecutor {
    override fun execute(entity: EntityMob): Boolean {
        return entity.memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.NEAREST_DOOR) && entity.memoryStorage!!
            .get<BlockWoodenDoor>(CoreMemoryTypes.Companion.NEAREST_DOOR).position.asVector3f() == entity.memoryStorage!!
            .get<Block>(CoreMemoryTypes.Companion.NEAREST_BLOCK_2).position.asVector3f()
    }

    override fun onStart(entity: EntityMob) {
        if (entity.memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.NEAREST_BLOCK_2)) {
            if (entity.memoryStorage!!.get<Block>(CoreMemoryTypes.Companion.NEAREST_BLOCK_2) is BlockWoodenDoor) {
                if (!door.isAir() && !door.isOpen()) door.toggle(null)
                entity.memoryStorage!!.set<BlockWoodenDoor>(CoreMemoryTypes.Companion.NEAREST_DOOR, door)
            }
        }
    }

    override fun onStop(entity: EntityMob) {
        if (entity.memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.NEAREST_DOOR)) {
            val door = entity.memoryStorage!!.get<BlockWoodenDoor>(CoreMemoryTypes.Companion.NEAREST_DOOR)
            if (door.levelBlock is BlockWoodenDoor)  //Can fail when the door gets broken
                door.toggle(null)
            entity.memoryStorage!!.clear(CoreMemoryTypes.Companion.NEAREST_DOOR)
        }
    }

    override fun onInterrupt(entity: EntityMob) {
        onStop(entity)
    }
}
