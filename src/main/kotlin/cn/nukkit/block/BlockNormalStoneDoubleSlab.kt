package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockNormalStoneDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {
    override val slabName: String
        get() = "Stone"

    override val singleSlab: BlockState
        get() = BlockNormalStoneSlab.Companion.PROPERTIES.getDefaultState()

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.NORMAL_STONE_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}