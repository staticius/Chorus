package org.chorus.entity.ai.executor

import org.chorus.block.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob
import lombok.AllArgsConstructor


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
