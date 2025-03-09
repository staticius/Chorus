package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBirchLog @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLog(blockstate) {
    override fun getStrippedState(): BlockState {
        return BlockStrippedBirchLog.properties.defaultState
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BIRCH_LOG, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}