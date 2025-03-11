package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPaleOakDoubleSlab(blockstate: BlockState?) : BlockDoubleWoodenSlab(blockstate) {
    override val slabName: String
        get() = "Acacia"

    override val singleSlab: BlockState
        get() = BlockAcaciaSlab.properties.defaultState

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PALE_OAK_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}