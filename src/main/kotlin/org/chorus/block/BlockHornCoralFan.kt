package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockHornCoralFan : BlockCoralFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Horn Coral Fan"

    override val deadCoralFan: Block
        get() = BlockDeadHornCoralFan()

    companion object {
        val properties: BlockProperties = BlockProperties(HORN_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
            get() = Companion.field
    }
}