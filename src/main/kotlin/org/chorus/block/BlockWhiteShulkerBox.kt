package org.chorus.block

import org.chorus.item.Item
import java.util.Set

class BlockWhiteShulkerBox @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockUndyedShulkerBox(blockstate) {
    override val shulkerBox: Item
        get() = ItemShulkerBox(0)

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WHITE_SHULKER_BOX, Set.of<String?>(BlockTags.PNX_SHULKERBOX))

    }
}