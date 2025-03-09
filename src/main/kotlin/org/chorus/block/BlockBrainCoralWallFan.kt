package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBrainCoralWallFan : BlockCoralWallFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Brain Coral"

    override fun getDeadCoralFan(): Block {
        return BlockDeadBrainCoralWallFan()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BRAIN_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)
            get() = Companion.field
    }
}