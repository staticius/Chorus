package org.chorus.block

import org.chorus.tags.BlockTags
import org.chorus.utils.DyeColor
import java.util.Set

class BlockPurpleWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.PURPLE
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_WOOL, Set.of(BlockTags.PNX_WOOL))
            get() = Companion.field
    }
}