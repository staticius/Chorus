package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedExposedCopperTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperTrapdoor(blockstate) {
    override val name: String
        get() = "Waxed Exposed Copper Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_EXPOSED_COPPER_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}