package org.chorus.block

import cn.nukkit.item.ItemTool

class BlockMossyCobblestone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Mossy Cobblestone"

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 10.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MOSSY_COBBLESTONE)
            get() = Companion.field
    }
}