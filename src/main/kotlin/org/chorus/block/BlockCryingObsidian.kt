package org.chorus.block

import org.chorus.item.ItemTool

class BlockCryingObsidian @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Crying Obsidian"

    override val hardness: Double
        get() = 50.0

    override val resistance: Double
        get() = 1200.0

    override val lightLevel: Int
        get() = 10

    override val toolTier: Int
        get() = ItemTool.TIER_DIAMOND

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CRYING_OBSIDIAN)

    }
}