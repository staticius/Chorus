package org.chorus.block

import org.chorus.tags.BlockTags
import org.chorus.utils.DyeColor
import java.util.Set

class BlockGrayWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.GRAY
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRAY_WOOL, Set.of(BlockTags.PNX_WOOL))

    }
}