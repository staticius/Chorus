package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockOrangeGlazedTerracotta @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlazedTerracotta(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.ORANGE_GLAZED_TERRACOTTA, CommonBlockProperties.FACING_DIRECTION)
    }
}