package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedCopperTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperTrapdoor(blockstate) {
    override val name: String
        get() = "Waxed Copper Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WAXED_COPPER_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}