package cn.nukkit.block

import cn.nukkit.item.*

class BlockInfestedMossyStoneBricks @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockState) {
    override val name: String
        get() = "Infested Mossy Stone Bricks"

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
        val properties: BlockProperties = BlockProperties(BlockID.Companion.INFESTED_MOSSY_STONE_BRICKS)
            get() = Companion.field
    }
}
