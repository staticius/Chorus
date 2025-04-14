package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.item.ItemTool

class BlockWarpedSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSlab(blockstate, BlockID.WARPED_DOUBLE_SLAB) {
    override fun getSlabName() = "Warped"

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



    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WARPED_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
    }
}