package org.chorus.block

import org.chorus.utils.DyeColor

class BlockGrayStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.GRAY

    companion object {
        val properties: BlockProperties = BlockProperties(GRAY_STAINED_GLASS)
            get() = Companion.field
    }
}