package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.ItemShulkerBox
import org.chorus.tags.BlockTags
import java.util.Set

class BlockMagentaShulkerBox @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(2)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_SHULKER_BOX, setOf(BlockTags.PNX_SHULKERBOX))

    }
}