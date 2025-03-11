package org.chorus.block

import org.chorus.item.ItemTool

class BlockChiseledNetherBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    override val name: String
        get() = "Chiseled Nether Bricks"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 6.0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_NETHER_BRICKS)

    }
}