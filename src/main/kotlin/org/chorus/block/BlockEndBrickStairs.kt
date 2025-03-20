package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockEndBrickStairs @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStairs(blockstate) {
    override val name: String
        get() = "End Stone Brick Stairs"

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 9.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.END_BRICK_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
    }
}