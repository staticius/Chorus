package org.chorus.entity.ai.route.posevaluator

import cn.nukkit.block.Block
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.SimpleAxisAlignedBB
import cn.nukkit.math.Vector3
import cn.nukkit.utils.Utils

/**
 * 用于游泳实体的坐标评估器
 */
class SwimmingPosEvaluator : IPosEvaluator {
    override fun evalPos(entity: EntityMob, pos: Vector3): Boolean {
        val blockId = entity.level!!.getTickCachedBlock(pos).id
        return isPassable(entity, pos) && (blockId === Block.FLOWING_WATER || blockId === Block.WATER)
    }

    override fun evalStandingBlock(entity: EntityMob, block: Block): Boolean {
        return true
    }

    /**
     * 指定实体在指定坐标上能否不发生碰撞
     * 对于空间中的移动做了特别的优化
     */
    protected fun isPassable(entity: EntityMob, vector3: Vector3): Boolean {
        val radius = (entity.width * entity.getScale()) * 0.5 + 0.1
        val height = entity.height * entity.getScale()
        // 原版中不会贴地飞行
        val bb: AxisAlignedBB = SimpleAxisAlignedBB(
            vector3.getX() - radius,
            vector3.getY() - height * 0.5,
            vector3.getZ() - radius,
            vector3.getX() + radius,
            vector3.getY() + height,
            vector3.getZ() + radius
        )
        return !Utils.hasCollisionTickCachedBlocks(entity.level, bb)
    }
}
