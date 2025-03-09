package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockSmoothStoneDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockDoubleSlabBase(blockstate) {
    override val slabName: String
        get() = "Smooth Stone"

    override val singleSlab: BlockState
        get() = BlockSmoothStoneSlab.Companion.PROPERTIES.getDefaultState()

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SMOOTH_STONE_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}