package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBubbleCoralFan : BlockCoralFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Bubble Coral Fan"

    override fun getDeadCoralFan(): Block {
        return BlockDeadBubbleCoralFan()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BUBBLE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
            get() = Companion.field
    }
}