package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockDeadTubeCoralFan : BlockCoralFanDead {
    constructor() : super(Companion.properties.defaultState)


    constructor(blockstate: BlockState?) : super(blockstate)

    companion object {
        val properties: BlockProperties =
            BlockProperties(DEAD_TUBE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
            get() = Companion.field
    }
}