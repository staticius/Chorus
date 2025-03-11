package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDeadFireCoralWallFan : BlockDeadCoralWallFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Fire Coral"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEAD_FIRE_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)

    }
}