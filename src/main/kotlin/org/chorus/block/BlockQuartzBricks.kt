package org.chorus.block

import org.chorus.item.ItemTool

class BlockQuartzBricks @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Quartz Bricks"

    override val hardness: Double
        get() = 0.8

    override val resistance: Double
        get() = 4.0

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.QUARTZ_BRICKS)

    }
}