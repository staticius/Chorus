package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDeadBrainCoralFan : BlockCoralFanDead {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    val wallFanId: String
        get() = DEAD_BRAIN_CORAL_WALL_FAN

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEAD_BRAIN_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)

    }
}