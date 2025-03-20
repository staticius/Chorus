package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDeadBubbleCoralFan : BlockCoralFanDead {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEAD_BUBBLE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
    }
}