package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBrainCoralFan : BlockCoralFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Brain Coral Fan"

    override fun getDeadCoralFan(): Block {
        return BlockDeadBrainCoralFan()
    }

    val wallFanId: String
        get() = BRAIN_CORAL_WALL_FAN

    companion object {
        val properties: BlockProperties = BlockProperties(BRAIN_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
            get() = Companion.field
    }
}