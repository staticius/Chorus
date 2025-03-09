package cn.nukkit.block

import cn.nukkit.tags.BlockTags
import cn.nukkit.utils.DyeColor
import java.util.Set

class BlockLightGrayWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.LIGHT_GRAY
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_GRAY_WOOL, Set.of(BlockTags.PNX_WOOL))
            get() = Companion.field
    }
}