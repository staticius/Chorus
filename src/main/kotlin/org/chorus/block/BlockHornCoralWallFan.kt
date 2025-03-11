package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockHornCoralWallFan : BlockCoralWallFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Horn Coral"

    override val deadCoralFan: Block
        get() = BlockDeadHornCoralWallFan()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HORN_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)

    }
}