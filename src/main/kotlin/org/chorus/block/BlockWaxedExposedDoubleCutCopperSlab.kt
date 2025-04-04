package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedExposedDoubleCutCopperSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockExposedDoubleCutCopperSlab(blockstate) {
    override val singleSlab: BlockState
        get() = BlockWaxedExposedCutCopperSlab.Companion.properties.defaultState

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WAXED_EXPOSED_DOUBLE_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}