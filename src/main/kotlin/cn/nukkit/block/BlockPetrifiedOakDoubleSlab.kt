package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPetrifiedOakDoubleSlab(blockstate: BlockState?) : BlockDoubleWoodenSlab(blockstate) {
    override val slabName: String
        get() = "Petrified Oak"

    override val singleSlab: BlockState
        get() = BlockPetrifiedOakSlab.Companion.PROPERTIES.getDefaultState()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PETRIFIED_OAK_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}