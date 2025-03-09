package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockRedSandstoneSlab(blockState: BlockState?) : BlockSlab(blockState, BlockID.RED_SANDSTONE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Red Sandstone"
    }

    override fun isSameType(slab: BlockSlab): Boolean {
        return slab.id == this.id
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.RED_SANDSTONE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}
