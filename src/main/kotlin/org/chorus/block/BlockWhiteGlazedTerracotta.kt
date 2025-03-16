package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWhiteGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockGlazedTerracotta(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WHITE_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)

    }
}