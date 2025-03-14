package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemShulkerBox
import org.chorus.tags.BlockTags
import java.util.Set

class BlockGreenShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(13)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GREEN_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))

    }
}