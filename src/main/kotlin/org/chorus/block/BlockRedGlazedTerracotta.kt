package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockRedGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlazedTerracotta(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.RED_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)
    }
}