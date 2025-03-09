package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPolishedBlackstoneBrickWall @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPolishedBlackstoneWall(blockstate) {
    override val name: String
        get() = "Polished Blackstone Brick Wall"

    override val hardness: Double
        get() = 1.5

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.POLISHED_BLACKSTONE_BRICK_WALL,
            CommonBlockProperties.WALL_CONNECTION_TYPE_EAST,
            CommonBlockProperties.WALL_CONNECTION_TYPE_NORTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_SOUTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_WEST,
            CommonBlockProperties.WALL_POST_BIT
        )
            get() = Companion.field
    }
}