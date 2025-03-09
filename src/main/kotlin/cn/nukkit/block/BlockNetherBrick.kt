package cn.nukkit.block

import cn.nukkit.item.ItemTool

/**
 * @author xtypr
 * @since 2015/12/7
 */
open class BlockNetherBrick : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Nether Brick"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 6.0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.NETHER_BRICK)
            get() = Companion.field
    }
}
