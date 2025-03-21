package org.chorus.block

import org.chorus.utils.DyeColor

class BlockOrangeStainedGlass @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override fun getDyeColor()  = DyeColor.ORANGE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_STAINED_GLASS)

    }
}