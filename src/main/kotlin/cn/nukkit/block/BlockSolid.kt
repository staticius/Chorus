package cn.nukkit.block

import cn.nukkit.math.BlockFace

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class BlockSolid(blockState: BlockState?) : Block(blockState) {
    override val isSolid: Boolean
        get() = true

    override fun isSolid(side: BlockFace): Boolean {
        return true
    }
}