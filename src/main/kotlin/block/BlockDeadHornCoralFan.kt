package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties

class BlockDeadHornCoralFan : BlockCoralFanDead {
    constructor() : super(properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEAD_HORN_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
    }
}