package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockMossyStoneBrickSlab(blockState: BlockState?) : BlockSlab(blockState, BlockID.MOSSY_STONE_BRICK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Mossy Stone Brick"
    }

    override fun isSameType(slab: BlockSlab): Boolean {
        return this.id == slab.id
    }

    override val hardness: Double
        get() = 1.5

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MOSSY_STONE_BRICK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}