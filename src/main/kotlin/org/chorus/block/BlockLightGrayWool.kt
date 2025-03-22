package org.chorus.block

import org.chorus.tags.BlockTags
import org.chorus.utils.DyeColor

class BlockLightGrayWool @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.LIGHT_GRAY
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_GRAY_WOOL, setOf(BlockTags.PNX_WOOL))
    }
}