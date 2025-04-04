package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWarpedStem @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStem(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.properties.getDefaultState()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WARPED_STEM, CommonBlockProperties.PILLAR_AXIS)

    }
}