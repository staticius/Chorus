package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockDeadFireCoralFan : BlockCoralFanDead {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties =
            BlockProperties(DEAD_FIRE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
            get() = Companion.field
    }
}