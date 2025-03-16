package org.chorus.block

import org.chorus.item.ItemTool

class BlockSmoothStone @JvmOverloads constructor(blockState: BlockState = Companion.properties.getDefaultState()) :
    BlockSolid(blockState) {
    override val name: String
        get() = "Smooth Stone"

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 10.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SMOOTH_STONE)

    }
}
