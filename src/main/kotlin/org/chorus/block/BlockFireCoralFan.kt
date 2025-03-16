package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockFireCoralFan : BlockCoralFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) :  super(blockstate)

    override val name: String
        get() = "Fire Coral Fan"

    override val deadCoralFan: Block
        get() = BlockDeadFireCoralFan()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.FIRE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)

    }
}