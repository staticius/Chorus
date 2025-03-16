package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDarkOakTrapdoor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Dark Oak Trapdoor"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.DARK_OAK_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}