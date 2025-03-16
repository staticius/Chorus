package org.chorus.block

import org.chorus.tags.BlockTags
import org.chorus.utils.DyeColor
import java.util.Set

class BlockLimeWool @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.LIME
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_WOOL, Set.of(BlockTags.PNX_WOOL))

    }
}