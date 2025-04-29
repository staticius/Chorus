package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties

class BlockFireCoralWallFan : BlockCoralWallFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val name: String
        get() = "Fire Coral"

    override fun getDeadCoralFan() = BlockDeadFireCoralWallFan()

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.FIRE_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)
    }
}