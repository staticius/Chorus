package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemTool

class BlockInfestedStone @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockState) {
    override val name: String
        get() = "Infested Stone"

    override val hardness: Double
        get() = 0.75

    override val resistance: Double
        get() = 0.75

    override fun getDrops(item: Item): Array<Item> {
        return Item.EMPTY_ARRAY
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.Companion.INFESTED_STONE)

    }
}
