package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedDoubleCutCopperSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockDoubleCutCopperSlab(blockstate) {
    override val singleSlab: BlockState
        get() = BlockWaxedCutCopperSlab.Companion.PROPERTIES.getDefaultState()

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WAXED_DOUBLE_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}