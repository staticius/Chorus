package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBlackGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlazedTerracotta(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BLACK_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)

    }
}