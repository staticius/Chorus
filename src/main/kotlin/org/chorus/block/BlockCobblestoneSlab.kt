package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockCobblestoneSlab(blockState: BlockState?) : BlockSlab(blockState, COBBLESTONE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Cobblestone"
    }

    override fun isSameType(slab: BlockSlab): Boolean {
        return this.id == slab.id
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
            BlockProperties(COBBLESTONE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}