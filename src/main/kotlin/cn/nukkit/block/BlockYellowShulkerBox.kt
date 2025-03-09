package cn.nukkit.block

import cn.nukkit.item.Item
import cn.nukkit.item.ItemShulkerBox
import cn.nukkit.tags.BlockTags
import java.util.Set

class BlockYellowShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockUndyedShulkerBox(blockstate) {
    override val shulkerBox: Item
        get() = ItemShulkerBox(4)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))
            get() = Companion.field
    }
}