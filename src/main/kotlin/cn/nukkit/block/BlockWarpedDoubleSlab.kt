package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool

class BlockWarpedDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockDoubleSlabBase(blockstate) {
    override val slabName: String
        get() = "Warped"

    override fun isCorrectTool(item: Item): Boolean {
        return true
    }

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 3.0

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    override val singleSlab: BlockState
        get() = BlockWarpedSlab.Companion.PROPERTIES.getDefaultState()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WARPED_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}