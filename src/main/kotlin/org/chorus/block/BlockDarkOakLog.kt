package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDarkOakLog @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLog(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedDarkOakLog.properties.defaultState
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DARK_OAK_LOG, CommonBlockProperties.PILLAR_AXIS)

    }
}