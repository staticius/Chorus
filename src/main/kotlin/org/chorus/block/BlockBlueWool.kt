package org.chorus.block

import org.chorus.tags.BlockTags
import org.chorus.utils.DyeColor
import java.util.Set

class BlockBlueWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BLUE
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLUE_WOOL, Set.of(BlockTags.PNX_WOOL))

    }
}