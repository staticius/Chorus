package org.chorus.block

import org.chorus.item.*

class BlockInfestedCrackedStoneBricks @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockState) {
    override val name: String
        get() = "Infested Cracked Stone Bricks"

    override val hardness: Double
        get() = 0.75

    override val resistance: Double
        get() = 0.75

    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.Companion.INFESTED_CRACKED_STONE_BRICKS)
            get() = Companion.field
    }
}
