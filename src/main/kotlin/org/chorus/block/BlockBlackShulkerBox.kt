package org.chorus.block

import cn.nukkit.item.*
import cn.nukkit.tags.BlockTags
import java.util.Set

class BlockBlackShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(15)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BLACK_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))
            get() = Companion.field
    }
}