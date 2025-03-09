package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockTubeCoralWallFan : BlockCoralWallFan {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Tube Coral"

    override val deadCoralFan: Block
        get() = BlockDeadTubeCoralWallFan()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.TUBE_CORAL_WALL_FAN, CommonBlockProperties.CORAL_DIRECTION)
            get() = Companion.field
    }
}