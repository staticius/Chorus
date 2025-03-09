package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool

class BlockMossyCobblestoneWall @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWallBase(blockstate) {
    override val name: String
        get() = "Mossy Cobblestone Wall"

    override val resistance: Double
        get() = 6.0

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun toItem(): Item? {
        return ItemBlock(properties.defaultState.toBlock())
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.MOSSY_COBBLESTONE_WALL,
            CommonBlockProperties.WALL_CONNECTION_TYPE_EAST,
            CommonBlockProperties.WALL_CONNECTION_TYPE_NORTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_SOUTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_WEST,
            CommonBlockProperties.WALL_POST_BIT
        )
            get() = Companion.field
    }
}
