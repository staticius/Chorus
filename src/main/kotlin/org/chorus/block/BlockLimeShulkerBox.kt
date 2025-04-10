package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemShulkerBox
import org.chorus.tags.BlockTags

class BlockLimeShulkerBox @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(5)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_SHULKER_BOX, setOf(BlockTags.PNX_SHULKERBOX))

    }
}