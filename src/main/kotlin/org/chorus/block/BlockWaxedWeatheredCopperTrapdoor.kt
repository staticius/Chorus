package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedWeatheredCopperTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperTrapdoor(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.BlockID.WAXED_WEATHERED_COPPER_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}