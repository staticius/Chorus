package org.chorus.block

import org.chorus.utils.DyeColor

class BlockPinkStainedGlass @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override fun getDyeColor()  = DyeColor.PINK

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_STAINED_GLASS)

    }
}