package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedWeatheredCutCopperSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWeatheredCutCopperSlab(blockstate, BlockID.WAXED_WEATHERED_DOUBLE_CUT_COPPER_SLAB) {
    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WAXED_WEATHERED_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}