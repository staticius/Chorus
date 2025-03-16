package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockMangroveLog : BlockLog {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Mangrove Log"

    override fun getStrippedState(): BlockState {
        return BlockStrippedMangroveLog.properties.defaultState
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MANGROVE_LOG, CommonBlockProperties.PILLAR_AXIS)

    }
}
