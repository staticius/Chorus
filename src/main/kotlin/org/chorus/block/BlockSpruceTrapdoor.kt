package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockSpruceTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockTrapdoor(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.SPRUCE_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
            get() = Companion.field
    }
}