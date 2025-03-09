package org.chorus.block

import cn.nukkit.tags.BlockTags
import cn.nukkit.utils.DyeColor
import java.util.Set

class BlockGreenWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.GREEN
    }

    companion object {
        val properties: BlockProperties = BlockProperties(GREEN_WOOL, Set.of(BlockTags.PNX_WOOL))
            get() = Companion.field
    }
}