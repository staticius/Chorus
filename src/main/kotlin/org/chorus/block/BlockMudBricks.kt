package org.chorus.block

import org.chorus.item.ItemTool

class BlockMudBricks @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate) {
    override val name: String
        get() = "Mud Bricks"

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 1.5

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MUD_BRICKS)

    }
}