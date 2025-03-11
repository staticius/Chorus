package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockColoredTorchRg @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.COLORED_TORCH_RG,
            CommonBlockProperties.COLOR_BIT,
            CommonBlockProperties.TORCH_FACING_DIRECTION
        )

    }
}