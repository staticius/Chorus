package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockUnderwaterTorch @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.UNDERWATER_TORCH, CommonBlockProperties.TORCH_FACING_DIRECTION)
            get() = Companion.field
    }
}