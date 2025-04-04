package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWeatheredCopperTrapdoor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperTrapdoor(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WEATHERED_COPPER_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}