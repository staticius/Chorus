package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties


class BlockLargeAmethystBud @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockAmethystBud(blockState) {
    override val namePrefix: String
        get() = "Large"

    override val lightLevel: Int
        get() = 4

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LARGE_AMETHYST_BUD, CommonBlockProperties.MINECRAFT_BLOCK_FACE)
            get() = Companion.field
    }
}
