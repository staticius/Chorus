package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDeadTubeCoralWallFan : BlockDeadCoralWallFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Tube Coral"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEAD_TUBE_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)

    }
}