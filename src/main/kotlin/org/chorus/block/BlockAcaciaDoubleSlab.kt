package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockAcaciaDoubleSlab(blockstate: BlockState?) : BlockDoubleWoodenSlab(blockstate) {
    override fun getSlabName(): String {
        return "Acacia"
    }

    override fun getSingleSlab(): BlockState {
        return BlockAcaciaSlab.Companion.PROPERTIES.getDefaultState()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(ACACIA_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}