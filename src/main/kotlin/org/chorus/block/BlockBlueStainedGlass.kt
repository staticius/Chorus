package org.chorus.block

import org.chorus.utils.DyeColor

class BlockBlueStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BLUE
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLUE_STAINED_GLASS)

    }
}