package org.chorus.block

import org.chorus.utils.DyeColor

class BlockLightGrayStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.LIGHT_GRAY

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_GRAY_STAINED_GLASS)
            get() = Companion.field
    }
}