package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemShulkerBox
import org.chorus.tags.BlockTags

class BlockGrayShulkerBox @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(7)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRAY_SHULKER_BOX, setOf(BlockTags.PNX_SHULKERBOX))

    }
}