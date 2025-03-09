package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockDeadBubbleCoralWallFan : BlockDeadCoralWallFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Bubble Coral"

    companion object {
        val properties: BlockProperties =
            BlockProperties(DEAD_BUBBLE_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)
            get() = Companion.field
    }
}