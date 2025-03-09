package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties


class BlockMudBrickStairs : BlockStairs {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Mud Bricks Stair"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.MUD_BRICK_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}
