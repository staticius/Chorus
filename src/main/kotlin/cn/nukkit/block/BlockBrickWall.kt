package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockBrickWall @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWallBase(blockstate) {
    override val name: String
        get() = "Brick Wall"

    override val resistance: Double
        get() = 6.0

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun toItem(): Item? {
        return ItemBlock(properties.defaultState.toBlock())
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BRICK_WALL,
            CommonBlockProperties.WALL_CONNECTION_TYPE_EAST,
            CommonBlockProperties.WALL_CONNECTION_TYPE_NORTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_SOUTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_WEST,
            CommonBlockProperties.WALL_POST_BIT
        )
            get() = Companion.field
    }
}
