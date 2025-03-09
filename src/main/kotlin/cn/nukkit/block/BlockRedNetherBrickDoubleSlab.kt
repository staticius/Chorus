package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockRedNetherBrickDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockDoubleSlabBase(blockstate) {
    override val slabName: String
        get() = "Red Nether Brick"

    override val singleSlab: BlockState
        get() = BlockRedNetherBrickSlab.Companion.PROPERTIES.getDefaultState()

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.RED_NETHER_BRICK_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}