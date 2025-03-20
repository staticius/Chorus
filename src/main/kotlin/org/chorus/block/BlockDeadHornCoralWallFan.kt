package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDeadHornCoralWallFan : BlockDeadCoralWallFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) :  super(blockstate)

    override val name: String
        get() = "Horn Coral"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEAD_HORN_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)
    }
}