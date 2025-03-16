package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockCrimsonHyphae @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStem(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.properties.defaultState
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CRIMSON_HYPHAE, CommonBlockProperties.PILLAR_AXIS)

    }
}