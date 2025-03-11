package org.chorus.block

import org.chorus.item.ItemTool

open class BlockHardenedClay @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 1.25

    override val resistance: Double
        get() = 7.0

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARDENED_CLAY)

    }
}