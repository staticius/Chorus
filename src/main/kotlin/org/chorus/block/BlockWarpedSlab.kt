package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool

class BlockWarpedSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSlab(blockstate, BlockID.WARPED_DOUBLE_SLAB) {
    override val slabName: String
        get() = "Warped"

    override fun canHarvestWithHand(): Boolean {
        return true
    }

    override val toolTier: Int
        get() = 0

    override fun isSameType(slab: BlockSlab): Boolean {
        return id == slab.id
    }

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val resistance: Double
        get() = 3.0

    override fun toItem(): Item? {
        return ItemBlock(this)
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WARPED_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}