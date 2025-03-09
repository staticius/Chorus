package org.chorus.block

import org.chorus.item.*
import org.chorus.tags.BlockTags
import java.util.Set

class BlockBrownShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(12)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BROWN_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))
            get() = Companion.field
    }
}