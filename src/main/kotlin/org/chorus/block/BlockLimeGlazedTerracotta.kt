package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockLimeGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlazedTerracotta(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIME_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)

    }
}