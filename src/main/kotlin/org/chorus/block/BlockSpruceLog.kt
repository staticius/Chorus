package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockSpruceLog @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockLog(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedSpruceLog.Companion.PROPERTIES.getDefaultState()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SPRUCE_LOG, CommonBlockProperties.PILLAR_AXIS)

    }
}