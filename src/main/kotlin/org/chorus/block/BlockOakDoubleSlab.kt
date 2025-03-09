package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockOakDoubleSlab(blockstate: BlockState?) : BlockDoubleWoodenSlab(blockstate) {
    override val slabName: String
        get() = "Oak"

    override val singleSlab: BlockState
        get() = BlockOakSlab.Companion.PROPERTIES.getDefaultState()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.OAK_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}