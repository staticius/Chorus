package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockExposedCopperTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperTrapdoor(blockstate) {
    override val name: String
        get() = "Exposed Copper Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.EXPOSED_COPPER_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}