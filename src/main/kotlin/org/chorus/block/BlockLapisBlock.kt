package org.chorus.block

import org.chorus.item.ItemTool

class BlockLapisBlock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Lapis Lazuli Block"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 5.0

    override val toolTier: Int
        get() = ItemTool.TIER_STONE

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LAPIS_BLOCK)
    }
}