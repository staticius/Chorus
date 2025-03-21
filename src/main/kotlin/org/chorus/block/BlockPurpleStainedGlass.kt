package org.chorus.block

import org.chorus.utils.DyeColor

class BlockPurpleStainedGlass @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override fun getDyeColor()  = DyeColor.PURPLE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_STAINED_GLASS)

    }
}