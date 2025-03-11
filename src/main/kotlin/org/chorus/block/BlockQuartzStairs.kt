package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockQuartzStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStairs(blockstate) {
    override val name: String
        get() = "Quartz Stairs"

    override val hardness: Double
        get() = 0.8

    override val resistance: Double
        get() = 4.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.QUARTZ_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )

    }
}