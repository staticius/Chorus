package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockStoneBrickStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStairs(blockstate) {
    override val name: String
        get() = "Stone Brick Stairs"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 30.0

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.STONE_BRICK_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}