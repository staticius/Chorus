package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockPolishedGraniteSlab(blockState: BlockState?) : BlockSlab(blockState, BlockID.POLISHED_GRANITE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Polished Granite"
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun isSameType(slab: BlockSlab): Boolean {
        return slab.id == this.id
    }

    override val hardness: Double
        get() = 1.5

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.POLISHED_GRANITE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}
