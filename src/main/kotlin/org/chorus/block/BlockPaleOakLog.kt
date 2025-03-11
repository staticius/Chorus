package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPaleOakLog @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLog(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedPaleOakLog.properties.defaultState
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PALE_OAK_LOG, CommonBlockProperties.PILLAR_AXIS)

    }
}