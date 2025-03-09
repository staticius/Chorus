package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

open class BlockPolishedBlackstoneWall @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWallBase(blockstate) {
    override val name: String
        get() = "Polished Blackstone Wall"

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 6.0

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.POLISHED_BLACKSTONE_WALL,
            CommonBlockProperties.WALL_CONNECTION_TYPE_EAST,
            CommonBlockProperties.WALL_CONNECTION_TYPE_NORTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_SOUTH,
            CommonBlockProperties.WALL_CONNECTION_TYPE_WEST,
            CommonBlockProperties.WALL_POST_BIT
        )
            get() = Companion.field
    }
}