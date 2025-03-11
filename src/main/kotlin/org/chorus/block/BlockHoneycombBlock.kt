package org.chorus.block

import org.chorus.item.ItemTool

class BlockHoneycombBlock : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val hardness: Double
        get() = 0.6

    override val resistance: Double
        get() = 0.6

    override val toolType: Int
        get() = ItemTool.TYPE_HANDS_ONLY

    override fun canHarvestWithHand(): Boolean {
        return true
    }

    override val name: String
        get() = "Honeycomb Block"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HONEYCOMB_BLOCK)

    }
}
