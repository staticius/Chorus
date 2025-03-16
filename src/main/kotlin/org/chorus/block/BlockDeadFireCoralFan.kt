package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDeadFireCoralFan : BlockCoralFanDead {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) :  super(blockstate)

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEAD_FIRE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)

    }
}