package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockMangroveTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.MANGROVE_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}