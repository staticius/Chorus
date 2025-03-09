package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedExposedCutCopperSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockExposedCutCopperSlab(blockstate, BlockID.WAXED_EXPOSED_DOUBLE_CUT_COPPER_SLAB) {
    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WAXED_EXPOSED_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}