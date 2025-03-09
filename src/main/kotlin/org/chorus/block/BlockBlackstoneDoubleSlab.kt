package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockBlackstoneDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {
    override fun getSlabName(): String {
        return "Blackstone"
    }

    override val resistance: Double
        get() = 6.0

    override val hardness: Double
        get() = 2.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun getSingleSlab(): BlockState {
        return BlockBlackstoneSlab.Companion.PROPERTIES.getDefaultState()
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BLACKSTONE_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}