package org.chorus.entity.ai.executor.villager

import cn.nukkit.block.BlockBed
import cn.nukkit.entity.ai.executor.EntityControl
import cn.nukkit.entity.ai.executor.IBehaviorExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.villagers.EntityVillagerV2
import cn.nukkit.level.*
import cn.nukkit.math.*

class SleepExecutor : EntityControl, IBehaviorExecutor {
    override fun execute(entity: EntityMob?): Boolean {
        return true
    }

    override fun onStart(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        if (entity.memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.OCCUPIED_BED)) {
            if (entity.memoryStorage!!.get<BlockBed>(CoreMemoryTypes.Companion.OCCUPIED_BED) is BlockBed) {
                val head: BlockBed = bed.getHeadPart()
                val foot: BlockBed = bed.getFootPart()

                val sleepingTransform = Transform(foot.locator).add(
                    when (head.blockFace) {
                        BlockFace.NORTH -> Vector3(0.5, 0.5625, 0.0)
                        BlockFace.SOUTH -> Vector3(0.5, 0.5625, 1.0)
                        BlockFace.WEST -> Vector3(0.0, 0.5625, 0.5)
                        BlockFace.EAST -> Vector3(1.0, 0.5625, 0.5)
                        else -> Vector3.ZERO
                    }
                )
                sleepingTransform.setYaw(BVector3.getYawFromVector(head.blockFace.opposite.unitVector))
                sleepingTransform.setHeadYaw(sleepingTransform.yaw)
                entity.teleport(sleepingTransform)
                entity.respawnToAll()
                entity.setDataFlag(EntityFlag.SLEEPING)
                entity.setDataProperty(EntityDataTypes.Companion.BED_POSITION, head.position.asBlockVector3())
                entity.setDataFlag(EntityFlag.BODY_ROTATION_BLOCKED)
            }
        }
    }

    override fun onStop(entity: EntityMob) {
        entity.setDataFlag(EntityFlag.SLEEPING, false)
        entity.setDataFlag(EntityFlag.BODY_ROTATION_BLOCKED, false)
        entity.setDataProperty(EntityDataTypes.Companion.BED_POSITION, BlockVector3(0, 0, 0))
        if (!entity.level!!.isNight) {
            if (entity is EntityVillagerV2) {
                entity.heal(entity.getMaxHealth().toFloat())
            }
        }
    }

    override fun onInterrupt(entity: EntityMob) {
        onStop(entity)
    }
}
