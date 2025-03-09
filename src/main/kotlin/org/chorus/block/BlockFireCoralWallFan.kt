package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockFireCoralWallFan : BlockCoralWallFan {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Fire Coral"

    override val deadCoralFan: Block
        get() = BlockDeadFireCoralWallFan()

    companion object {
        val properties: BlockProperties = BlockProperties(FIRE_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)
            get() = Companion.field
    }
}