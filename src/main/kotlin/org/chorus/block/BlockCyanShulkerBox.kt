package org.chorus.block

import org.chorus.item.*
import java.util.Set

class BlockCyanShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(9)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CYAN_SHULKER_BOX, Set.of<String>(BlockTags.PNX_SHULKERBOX))
            get() = Companion.field
    }
}