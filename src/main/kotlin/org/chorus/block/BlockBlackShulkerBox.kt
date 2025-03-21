package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemShulkerBox
import org.chorus.tags.BlockTags

class BlockBlackShulkerBox @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {

    override fun getShulkerBox(): Item {
        return ItemShulkerBox(15)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLACK_SHULKER_BOX, setOf(BlockTags.PNX_SHULKERBOX))
    }
}