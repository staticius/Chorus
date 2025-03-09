package cn.nukkit.block

import cn.nukkit.item.Item
import cn.nukkit.item.ItemShulkerBox
import cn.nukkit.tags.BlockTags
import java.util.Set

class BlockMagentaShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(2)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))
            get() = Companion.field
    }
}