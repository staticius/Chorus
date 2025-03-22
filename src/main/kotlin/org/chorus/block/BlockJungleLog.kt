package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockJungleLog @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockLog(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedJungleLog.properties.defaultState
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.JUNGLE_LOG, CommonBlockProperties.PILLAR_AXIS)
    }
}