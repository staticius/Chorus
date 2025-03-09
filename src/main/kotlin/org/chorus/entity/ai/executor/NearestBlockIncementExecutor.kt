package org.chorus.entity.ai.executor

import cn.nukkit.block.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.mob.EntityMob
import lombok.AllArgsConstructor

@AllArgsConstructor
class NearestBlockIncementExecutor : IBehaviorExecutor {
    override fun execute(entity: EntityMob): Boolean {
        if (!entity.memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.NEAREST_BLOCK)) {
            entity.memoryStorage!!.put<Block>(
                CoreMemoryTypes.Companion.NEAREST_BLOCK, entity.memoryStorage!!
                    .get<Block>(CoreMemoryTypes.Companion.NEAREST_BLOCK).up()
            )
        }
        return true
    }
}
