package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.level.Level
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.BlockFace

/**
 * @author xtypr
 * @since 2015/11/24
 */
abstract class BlockCarpet(blockState: BlockState?) : BlockFlowable(blockState) {
    override val hardness: Double
        get() = 0.1

    override val resistance: Double
        get() = 0.5

    override val isSolid: Boolean
        get() = false

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override fun canPassThrough(): Boolean {
        return false
    }

    override val waterloggingLevel: Int
        get() = 1

    override fun recalculateBoundingBox(): AxisAlignedBB? {
        return this
    }

    override var maxY: Double
        get() = position.y + 0.0625
        set(maxY) {
            super.maxY = maxY
        }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        val down = this.down()
        if (!down!!.isAir) {
            level.setBlock(block.position, this, true, true)
            return true
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (down()!!.isAir) {
                level.useBreakOn(this.position)

                return Level.BLOCK_UPDATE_NORMAL
            }
        }
        return 0
    }
}
