package org.chorus.block

import org.chorus.tags.BlockTags
import org.chorus.utils.DyeColor

class BlockBlackWool @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BLACK
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLACK_WOOL, mutableSetOf(BlockTags.PNX_WOOL))
    }
}