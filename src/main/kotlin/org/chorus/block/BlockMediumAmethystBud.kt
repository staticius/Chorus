package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockMediumAmethystBud @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockAmethystBud(blockState) {
    override val namePrefix: String
        get() = "Medium"

    override val lightLevel: Int
        get() = 2

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MEDIUM_AMETHYST_BUD, CommonBlockProperties.MINECRAFT_BLOCK_FACE)
            get() = Companion.field
    }
}
