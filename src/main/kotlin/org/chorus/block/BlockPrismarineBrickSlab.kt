package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockPrismarineBrickSlab(blockState: BlockState) : BlockSlab(blockState, BlockID.PRISMARINE_BRICK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Prismarine Brick"
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

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PRISMARINE_BRICK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}
