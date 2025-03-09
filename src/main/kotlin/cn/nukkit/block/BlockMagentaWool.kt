package cn.nukkit.block

import cn.nukkit.tags.BlockTags
import cn.nukkit.utils.DyeColor
import java.util.Set

class BlockMagentaWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.MAGENTA
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_WOOL, Set.of(BlockTags.PNX_WOOL))
            get() = Companion.field
    }
}