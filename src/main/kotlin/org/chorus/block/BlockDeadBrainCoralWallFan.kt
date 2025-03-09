package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockDeadBrainCoralWallFan : BlockDeadCoralWallFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Brain Coral"

    companion object {
        val properties: BlockProperties =
            BlockProperties(DEAD_BRAIN_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)
            get() = Companion.field
    }
}