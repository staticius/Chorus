package org.chorus.entity.ai.executor

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob


class NearestBlockIncementExecutor : IBehaviorExecutor {
    override fun execute(entity: EntityMob): Boolean {
        if (!entity.memoryStorage.isEmpty(CoreMemoryTypes.NEAREST_BLOCK)) {
            entity.memoryStorage.set(
                CoreMemoryTypes.NEAREST_BLOCK,
                entity.memoryStorage.get(CoreMemoryTypes.NEAREST_BLOCK)?.up()
            )
        }
        return true
    }
}
