package org.chorus.block

import org.chorus.tags.BlockTags
import org.chorus.utils.DyeColor
import java.util.Set

class BlockYellowWool @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor()  = DyeColor.YELLOW

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_WOOL, Set.of(BlockTags.PNX_WOOL))

    }
}