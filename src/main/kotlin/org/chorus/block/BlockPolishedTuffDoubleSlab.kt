package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockPolishedTuffDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {
    override val name: String
        get() = "Polished Tuff Double Slab"

    override val slabName: String
        get() = "Polished Tuff"

    override val singleSlab: BlockState
        get() = BlockPolishedTuffSlab.Companion.PROPERTIES.getDefaultState()

    override val resistance: Double
        get() = 6.0

    override val hardness: Double
        get() = 1.5

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.POLISHED_TUFF_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}