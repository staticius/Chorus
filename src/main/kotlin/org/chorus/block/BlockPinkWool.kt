package org.chorus.block

import org.chorus.tags.BlockTags
import org.chorus.utils.DyeColor
import java.util.Set

class BlockPinkWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.PINK
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_WOOL, Set.of(BlockTags.PNX_WOOL))
            get() = Companion.field
    }
}