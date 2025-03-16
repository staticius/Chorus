package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

open class BlockWeatheredDoubleCutCopperSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockDoubleCutCopperSlab(blockstate) {
    override val singleSlab: BlockState
        get() = BlockWeatheredCutCopperSlab.Companion.PROPERTIES.getDefaultState()

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WEATHERED_DOUBLE_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}