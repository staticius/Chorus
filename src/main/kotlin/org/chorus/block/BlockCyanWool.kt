package org.chorus.block

import java.util.Set

class BlockCyanWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.CYAN
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CYAN_WOOL, Set.of<String>(BlockTags.PNX_WOOL))

    }
}