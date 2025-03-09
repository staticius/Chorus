package org.chorus.block

import org.chorus.tags.BlockTags
import java.util.Set

class BlockLightBlueShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIGHT_BLUE_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))
            get() = Companion.field
    }
}