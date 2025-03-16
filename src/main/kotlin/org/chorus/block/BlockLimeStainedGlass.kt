package org.chorus.block

import org.chorus.utils.DyeColor

class BlockLimeStainedGlass @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.LIME

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_STAINED_GLASS)

    }
}