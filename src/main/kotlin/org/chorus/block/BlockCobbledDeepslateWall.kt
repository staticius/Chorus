package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockCobbledDeepslateWall @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWallBase(blockstate) {
    override val name: String
        get() = "Cobbled Deepslate Wall"

    override val resistance: Double
        get() = 6.0

    override val hardness: Double
        get() = 3.5

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    companion object {
        val properties: BlockProperties = BlockProperties(
            COBBLED_DEEPSLATE_WALL,
            CommonBlockProperties.WALL_CONNECTION_TYPE_EAST,
            CommonBlockProperties.WALL_CONNECTION_TYPE_NORTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_SOUTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_WEST,
            CommonBlockProperties.WALL_POST_BIT
        )
            get() = Companion.field
    }
}