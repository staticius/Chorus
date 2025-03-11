package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemShulkerBox
import org.chorus.tags.BlockTags
import java.util.Set

class BlockYellowShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockUndyedShulkerBox(blockstate) {
    override val shulkerBox: Item
        get() = ItemShulkerBox(4)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))

    }
}