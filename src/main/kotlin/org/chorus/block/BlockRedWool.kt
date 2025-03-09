package org.chorus.block

import java.util.Set

class BlockRedWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.RED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_WOOL, Set.of<String?>(BlockTags.PNX_WOOL))
            get() = Companion.field
    }
}