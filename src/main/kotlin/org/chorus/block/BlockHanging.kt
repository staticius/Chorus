package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace
import cn.nukkit.tags.BlockTags

abstract class BlockHanging(blockState: BlockState?) : BlockFlowable(blockState) {
    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL && !isSupportValid) {
            level.useBreakOn(this.position)
            return type
        }
        return 0
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
        return isSupportValid && super.place(item, block, target, face, fx, fy, fz, player)
    }

    protected open val isSupportValid: Boolean
        get() = down()!!.`is`(BlockTags.DIRT) || when (down()!!.id) {
            WARPED_NYLIUM, CRIMSON_NYLIUM, SOUL_SOIL -> true
            else -> false
        }

    override val burnChance: Int
        get() = 5
}
