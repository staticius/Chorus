package org.chorus.block

import org.chorus.tags.BlockTags
import org.chorus.utils.DyeColor

class BlockYellowWool @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockWool(blockstate) {
    override fun getDyeColor() = DyeColor.YELLOW

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_WOOL, setOf(BlockTags.PNX_WOOL))
    }
}