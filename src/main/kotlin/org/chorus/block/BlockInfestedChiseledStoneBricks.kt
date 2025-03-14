package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemTool

class BlockInfestedChiseledStoneBricks @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockState) {
    override val name: String
        get() = "Infested Chiseled Stone Bricks"

    override val hardness: Double
        get() = 0.75

    override val resistance: Double
        get() = 0.75

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun getDrops(item: Item): Array<Item> {
        return Item.EMPTY_ARRAY
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.Companion.INFESTED_CHISELED_STONE_BRICKS)

    }
}
