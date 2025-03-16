package org.chorus.block

import org.chorus.item.ItemTool

open class BlockEndStone : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "End Stone"

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

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.END_STONE)

    }
}
