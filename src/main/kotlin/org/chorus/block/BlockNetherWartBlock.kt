package org.chorus.block

import org.chorus.item.ItemTool

class BlockNetherWartBlock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Nether Wart Block"

    override val resistance: Double
        get() = 5.0

    override val hardness: Double
        get() = 1.0

    override val toolType: Int
        get() = ItemTool.TYPE_HOE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.NETHER_WART_BLOCK)

    }
}
