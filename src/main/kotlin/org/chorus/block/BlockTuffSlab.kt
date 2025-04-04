package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockTuffSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSlab(blockstate, BlockID.TUFF_DOUBLE_SLAB) {
    override val slabName: String
        get() = "Tuff"

    override fun isSameType(slab: BlockSlab): Boolean {
        return id == slab.id
    }

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 6.0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.TUFF_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}