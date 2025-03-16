package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedCutCopperSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockCutCopperSlab(blockstate, BlockID.WAXED_DOUBLE_CUT_COPPER_SLAB) {
    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WAXED_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}