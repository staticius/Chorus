package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockSpruceLog @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockLog(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedSpruceLog.Companion.properties.defaultState
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SPRUCE_LOG, CommonBlockProperties.PILLAR_AXIS)
    }
}