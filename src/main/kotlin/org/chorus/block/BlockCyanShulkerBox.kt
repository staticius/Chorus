package org.chorus.block

import org.chorus.item.*
import org.chorus.tags.BlockTags

class BlockCyanShulkerBox @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(9)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CYAN_SHULKER_BOX, setOf(BlockTags.PNX_SHULKERBOX))
    }
}