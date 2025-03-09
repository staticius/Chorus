package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockDeadBubbleCoralFan : BlockCoralFanDead {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties =
            BlockProperties(DEAD_BUBBLE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
            get() = Companion.field
    }
}