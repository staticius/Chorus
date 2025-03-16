package org.chorus.block

import org.chorus.item.ItemTool

class BlockFletchingTable @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Fletching Table"

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val resistance: Double
        get() = 12.5

    override val hardness: Double
        get() = 2.5

    override val burnChance: Int
        get() = 5

    override fun canHarvestWithHand(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.FLETCHING_TABLE)

    }
}
