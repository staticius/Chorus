package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockNetherBrickDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {
    override val slabName: String
        get() = "Nether Brick"

    override val singleSlab: BlockState
        get() = BlockNetherBrickSlab.Companion.PROPERTIES.getDefaultState()

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.NETHER_BRICK_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}