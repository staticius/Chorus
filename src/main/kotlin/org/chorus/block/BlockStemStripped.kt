package org.chorus.block

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.math.BlockFace

abstract class BlockStemStripped(blockstate: BlockState?) : BlockStem(blockstate) {
    override fun getStrippedState(): BlockState {
        return blockState!!
    }

    override fun canBeActivated(): Boolean {
        return false
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        return false
    }
}
