package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockCrimsonStem @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStem(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.properties.defaultState
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CRIMSON_STEM, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}