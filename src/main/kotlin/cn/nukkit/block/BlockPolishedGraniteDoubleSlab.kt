package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockPolishedGraniteDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {
    override val slabName: String
        get() = "Polished Granite"

    override val singleSlab: BlockState
        get() = BlockPolishedGraniteSlab.Companion.PROPERTIES.getDefaultState()

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.POLISHED_GRANITE_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}