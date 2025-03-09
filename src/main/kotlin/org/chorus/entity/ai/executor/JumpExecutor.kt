package org.chorus.entity.ai.executor

import org.chorus.entity.mob.EntityMob
import org.chorus.math.Vector3
import lombok.AllArgsConstructor


class JumpExecutor : IBehaviorExecutor {
    override fun execute(entity: EntityMob): Boolean {
        entity.setMotion(Vector3(0.0, entity.getJumpingMotion(0.4), 0.0))
        return true
    }
}
