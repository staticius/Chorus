package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockSmoothQuartzSlab(blockState: BlockState?) : BlockSlab(blockState, BlockID.SMOOTH_QUARTZ_DOUBLE_SLAB) {
    override val slabName: String
        get() = "Smooth Quartz"

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun isSameType(slab: BlockSlab): Boolean {
        return this.id == slab.id
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SMOOTH_QUARTZ_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}