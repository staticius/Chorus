package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockPrismarineSlab(blockState: BlockState?) : BlockSlab(blockState, BlockID.PRISMARINE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Prismarine"
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
            BlockProperties(BlockID.PRISMARINE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}
