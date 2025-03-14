package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemTool


class BlockStone @JvmOverloads constructor(blockState: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(blockState) {
    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf(get(BlockID.COBBLESTONE).toItem())
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.STONE)

    }
}
