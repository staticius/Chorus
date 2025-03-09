package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockSmoothStoneSlab(blockState: BlockState?) : BlockSlab(blockState, BlockID.SMOOTH_STONE_DOUBLE_SLAB) {
    override val slabName: String
        get() = "Smooth Stone"

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
            BlockProperties(BlockID.SMOOTH_STONE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}