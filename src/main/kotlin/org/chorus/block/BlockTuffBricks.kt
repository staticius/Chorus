package org.chorus.block

import cn.nukkit.item.ItemTool

class BlockTuffBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Tuff Bricks"

    override val resistance: Double
        get() = 6.0

    override val hardness: Double
        get() = 1.5

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.TUFF_BRICKS)
            get() = Companion.field
    }
}