package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemShulkerBox
import org.chorus.tags.BlockTags
import java.util.Set

class BlockLightGrayShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(8)
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIGHT_GRAY_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))

    }
}