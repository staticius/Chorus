package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockSmoker @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockLitSmoker(blockstate) {
    override val name: String
        get() = "Smoker"

    override val lightLevel: Int
        get() = 0

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SMOKER, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)

    }
}
