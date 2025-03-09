package cn.nukkit.entity.ai.controller

import cn.nukkit.entity.EntityPhysical
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.animal.EntityRabbit
import cn.nukkit.entity.mob.monster.EntitySlime
import cn.nukkit.level.Sound
import cn.nukkit.math.*
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * 处理陆地行走实体运动
 * todo: 有待解耦
 */
class HoppingController(moveCooldown: Int) : WalkController() {
    protected var moveCooldown: Int = 0

    override var currentJumpCoolDown: Int = 0

    init {
        this.moveCooldown = moveCooldown
    }

    override fun control(entity: EntityMob): Boolean {
        currentJumpCoolDown++
        if (entity.hasMoveDirection() && !entity.isShouldUpdateMoveDirection && currentJumpCoolDown > moveCooldown) {
            //clone防止异步导致的NPE
            val direction = entity.moveDirectionEnd.clone()
            val speed = entity.movementSpeed
            if (entity.motion.x * entity.motion.x + entity.motion.z * entity.motion.z > speed * speed * 0.4756) {
                entity.setDataFlag(EntityFlag.MOVING, false)
                return false
            }
            val relativeVector = direction.clone().setComponents(
                direction.x - entity.position.x,
                direction.y - entity.position.y, direction.z - entity.position.z
            )
            val xzLengthSquared = relativeVector.x * relativeVector.x + relativeVector.z * relativeVector.z
            if (abs(xzLengthSquared) < EntityPhysical.Companion.PRECISION) {
                entity.setDataFlag(EntityFlag.MOVING, false)
                return false
            }
            val xzLength = sqrt(relativeVector.x * relativeVector.x + relativeVector.z * relativeVector.z)
            val k = speed / xzLength * 0.33
            val dx = relativeVector.x * k
            val dz = relativeVector.z * k
            var dy = 0.0
            if (entity.isOnGround) {
                val diffY = entity.getScale().toDouble()
                dy += entity.getJumpingMotion(diffY)
                val jumpSound =
                    if (entity is EntityRabbit) Sound.MOB_RABBIT_HOP else if (entity is EntitySlime) Sound.JUMP_SLIME else null
                if (jumpSound != null) entity.level!!.addSound(entity.position, jumpSound)
                entity.setDataProperty(EntityDataTypes.Companion.CLIENT_EVENT, 2)
                currentJumpCoolDown = 0
            }
            entity.addTmpMoveMotion(Vector3(dx, dy, dz))
            entity.setDataFlag(EntityFlag.MOVING, true)
            if (xzLength < speed) {
                needNewDirection(entity)
                return false
            }
            return true
        } else {
            entity.setDataFlag(EntityFlag.MOVING, false)
            return false
        }
    }
}
