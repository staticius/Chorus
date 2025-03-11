package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedWeatheredDoubleCutCopperSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWeatheredDoubleCutCopperSlab(blockstate) {
    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.WAXED_WEATHERED_DOUBLE_CUT_COPPER_SLAB,
            CommonBlockProperties.MINECRAFT_VERTICAL_HALF
        )

    }
}