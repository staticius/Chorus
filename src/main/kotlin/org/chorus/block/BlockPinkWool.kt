package org.chorus.block

import org.chorus.tags.BlockTags
import org.chorus.utils.DyeColor

class BlockPinkWool @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.PINK
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_WOOL, setOf(BlockTags.PNX_WOOL))
    }
}