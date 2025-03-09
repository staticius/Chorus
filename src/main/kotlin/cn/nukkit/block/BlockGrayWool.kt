package cn.nukkit.block

import cn.nukkit.tags.BlockTags
import cn.nukkit.utils.DyeColor
import java.util.Set

class BlockGrayWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.GRAY
    }

    companion object {
        val properties: BlockProperties = BlockProperties(GRAY_WOOL, Set.of(BlockTags.PNX_WOOL))
            get() = Companion.field
    }
}