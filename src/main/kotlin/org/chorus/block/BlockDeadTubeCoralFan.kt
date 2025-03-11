package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDeadTubeCoralFan : BlockCoralFanDead {
    constructor() : super(Companion.properties.defaultState)


    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEAD_TUBE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)

    }
}