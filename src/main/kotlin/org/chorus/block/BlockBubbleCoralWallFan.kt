package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBubbleCoralWallFan : BlockCoralWallFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Bubble Coral"

    override fun getDeadCoralFan(): Block {
        return BlockDeadBubbleCoralWallFan()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BUBBLE_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)
            get() = Companion.field
    }
}