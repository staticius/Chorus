package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedWeatheredDoubleCutCopperSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWeatheredDoubleCutCopperSlab(blockstate) {
    override val isWaxed: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_WEATHERED_DOUBLE_CUT_COPPER_SLAB,
            CommonBlockProperties.MINECRAFT_VERTICAL_HALF
        )
    }
}