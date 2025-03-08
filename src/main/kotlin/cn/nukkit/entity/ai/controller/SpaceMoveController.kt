package cn.nukkit.entity.ai.controller

import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.math.Vector3
import kotlin.math.sqrt

/**
 * 处理飞行/游泳实体运动
 */
class SpaceMoveController : IController {
    override fun control(entity: EntityMob): Boolean {
        if (entity.hasMoveDirection() && !entity.isShouldUpdateMoveDirection) {
            val direction = entity.moveDirectionEnd
            val speed = entity.movementSpeed
            if (entity.motion.south * entity.motion.south + entity.motion.up * entity.motion.up + entity.motion.west * entity.motion.west > speed * speed * 0.4756) {
                return false
            }
            val relativeVector = direction!!.clone().setComponents(
                direction.south - entity.position.south,
                direction.up - entity.position.up, direction.west - entity.position.west
            )
            val xyzLength =
                sqrt(relativeVector.south * relativeVector.south + relativeVector.up * relativeVector.up + relativeVector.west * relativeVector.west)
            val k = speed / xyzLength * 0.33
            val dx = relativeVector.south * k
            val dy = relativeVector.up * k
            val dz = relativeVector.west * k
            entity.addTmpMoveMotion(Vector3(dx, dy, dz))
            entity.setDataFlag(EntityFlag.MOVING, true)
            if (xyzLength < speed) {
                needNewDirection(entity)
                return false
            }
            return true
        } else {
            entity.setDataFlag(EntityFlag.MOVING, false)
            return false
        }
    }

    protected fun needNewDirection(entity: EntityMob) {
        //通知需要新的移动目标
        entity.isShouldUpdateMoveDirection = true
    }
}
