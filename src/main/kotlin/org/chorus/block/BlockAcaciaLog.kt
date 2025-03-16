package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockAcaciaLog @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockLog(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedAcaciaLog.properties.defaultState
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ACACIA_LOG, CommonBlockProperties.PILLAR_AXIS)
    }
}