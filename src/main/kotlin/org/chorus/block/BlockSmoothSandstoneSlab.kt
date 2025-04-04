package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockSmoothSandstoneSlab(blockState: BlockState) : BlockSlab(blockState, BlockID.SMOOTH_SANDSTONE_DOUBLE_SLAB) {
    override fun getSlabName() = "Smooth Sandstone"

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

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SMOOTH_SANDSTONE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}
