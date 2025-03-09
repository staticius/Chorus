package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockPolishedDioriteSlab(blockState: BlockState?) : BlockSlab(blockState, BlockID.POLISHED_DIORITE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Polished Diorite"
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
            BlockProperties(BlockID.POLISHED_DIORITE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}
