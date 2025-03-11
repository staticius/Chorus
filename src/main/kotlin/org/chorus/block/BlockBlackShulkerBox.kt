package org.chorus.block

import org.chorus.item.*
import org.chorus.tags.BlockTags
import java.util.Set

class BlockBlackShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {

    override fun getShulkerBox(): Item {
        return ItemShulkerBox(15)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLACK_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))
    }
}