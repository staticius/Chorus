package org.chorus.block

import org.chorus.item.*
import org.chorus.tags.BlockTags
import java.util.Set

class BlockBlueShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(11)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLUE_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))

    }
}