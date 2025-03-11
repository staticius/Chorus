package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockAcaciaTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Acacia Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.ACACIA_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}