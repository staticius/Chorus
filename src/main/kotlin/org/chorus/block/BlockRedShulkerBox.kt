package org.chorus.block

import cn.nukkit.item.Item
import java.util.Set

class BlockRedShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(14)
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.RED_SHULKER_BOX, Set.of<String?>(BlockTags.PNX_SHULKERBOX))
            get() = Companion.field
    }
}