package org.chorus.block

import org.chorus.item.ItemTool

class BlockDripstoneBlock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Dripstone Block"

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 1.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val isLavaResistant: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DRIPSTONE_BLOCK)

    }
}