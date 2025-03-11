package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockSoulTorch @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockTorch(blockstate) {
    override val name: String
        get() = "Soul Torch"

    override val lightLevel: Int
        get() = 10

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SOUL_TORCH, CommonBlockProperties.TORCH_FACING_DIRECTION)

    }
}
