package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockPolishedAndesiteSlab(blockState: BlockState?) :
    BlockSlab(blockState, BlockID.POLISHED_ANDESITE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Polished Andesite"
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

    override val hardness: Double
        get() = 1.5

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.POLISHED_ANDESITE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}
