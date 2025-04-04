package org.chorus.block

import org.chorus.item.ItemShulkerBox
import org.chorus.tags.BlockTags

class BlockWhiteShulkerBox @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox() = ItemShulkerBox(0)

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WHITE_SHULKER_BOX, setOf(BlockTags.PNX_SHULKERBOX))
    }
}