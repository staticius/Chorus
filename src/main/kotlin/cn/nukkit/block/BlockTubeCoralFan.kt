package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockTubeCoralFan : BlockCoralFan {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Tube Coral Fan"

    override val deadCoralFan: Block
        get() = BlockDeadTubeCoralFan()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.TUBE_CORAL_FAN, CommonBlockProperties.CORAL_FAN_DIRECTION)
            get() = Companion.field
    }
}