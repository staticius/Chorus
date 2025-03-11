package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockMossyCobblestoneStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairs(blockstate) {
    override val name: String
        get() = "Mossy Cobblestone Stairs"

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 30.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.MOSSY_COBBLESTONE_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )

    }
}