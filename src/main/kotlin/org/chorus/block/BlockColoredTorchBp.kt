package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockColoredTorchBp @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.COLORED_TORCH_BP,
            CommonBlockProperties.COLOR_BIT,
            CommonBlockProperties.TORCH_FACING_DIRECTION
        )
            get() = Companion.field
    }
}