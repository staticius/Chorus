package org.chorus.block

import org.chorus.item.ItemTool

open class BlockResinBricks @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate) {
    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RESIN_BRICKS)

    }
}