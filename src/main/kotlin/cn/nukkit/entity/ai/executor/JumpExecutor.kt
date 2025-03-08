package cn.nukkit.entity.ai.executor

import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.math.Vector3
import lombok.AllArgsConstructor

@AllArgsConstructor
class JumpExecutor : IBehaviorExecutor {
    override fun execute(entity: EntityMob): Boolean {
        entity.setMotion(Vector3(0.0, entity.getJumpingMotion(0.4), 0.0))
        return true
    }
}
