package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockFireCoralFan : BlockCoralFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Fire Coral Fan"

    override val deadCoralFan: Block
        get() = BlockDeadFireCoralFan()

    companion object {
        val properties: BlockProperties = BlockProperties(FIRE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
            get() = Companion.field
    }
}