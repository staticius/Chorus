package org.chorus.block

import java.util.Set

class BlockWhiteWool @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockWool(blockstate) {
    override fun getDyeColor()  = DyeColor.WHITE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_WOOL, Set.of<String?>(BlockTags.PNX_WOOL))

    }
}