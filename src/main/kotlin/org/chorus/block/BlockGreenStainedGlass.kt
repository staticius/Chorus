package org.chorus.block

import org.chorus.utils.DyeColor

class BlockGreenStainedGlass @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.GREEN

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GREEN_STAINED_GLASS)

    }
}