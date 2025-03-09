package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDarkOakDoubleSlab(blockstate: BlockState?) : BlockDoubleWoodenSlab(blockstate) {
    override val slabName: String
        get() = "Dark Oak"

    override val singleSlab: BlockState?
        get() = BlockDarkOakSlab.Companion.PROPERTIES.getDefaultState()

    companion object {
        val properties: BlockProperties =
            BlockProperties(DARK_OAK_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}