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
        get() = BlockID.BRAIN_CORAL_WALL_FAN

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BRAIN_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
    }
}