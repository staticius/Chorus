package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockSmallAmethystBud @JvmOverloads constructor(blockState: BlockState? = Companion.properties.getDefaultState()) :
    BlockAmethystBud(blockState) {
    override val namePrefix: String
        get() = "Small"

    override val lightLevel: Int
        get() = 1

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SMALL_AMETHYST_BUD, CommonBlockProperties.MINECRAFT_BLOCK_FACE)

    }
}
