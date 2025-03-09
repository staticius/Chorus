package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockBrickSlab(blockState: BlockState?) : BlockSlab(blockState, BRICK_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Brick"
    }

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
        val properties: BlockProperties = BlockProperties(BRICK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}