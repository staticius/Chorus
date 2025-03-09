package cn.nukkit.block

import cn.nukkit.tags.BlockTags
import cn.nukkit.utils.DyeColor
import java.util.Set

class BlockYellowWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWool(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.YELLOW

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_WOOL, Set.of(BlockTags.PNX_WOOL))
            get() = Companion.field
    }
}