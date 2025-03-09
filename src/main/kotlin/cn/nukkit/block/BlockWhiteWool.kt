package cn.nukkit.block

import java.util.Set

class BlockWhiteWool @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWool(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.WHITE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_WOOL, Set.of<String?>(BlockTags.PNX_WOOL))
            get() = Companion.field
    }
}