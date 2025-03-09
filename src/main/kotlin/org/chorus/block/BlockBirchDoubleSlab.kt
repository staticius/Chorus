package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBirchDoubleSlab(blockstate: BlockState?) : BlockDoubleWoodenSlab(blockstate) {
    override fun getSlabName(): String {
        return "Birch"
    }

    override fun getSingleSlab(): BlockState {
        return BlockBirchSlab.Companion.PROPERTIES.getDefaultState()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BIRCH_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}