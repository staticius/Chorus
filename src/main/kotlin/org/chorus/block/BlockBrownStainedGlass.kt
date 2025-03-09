package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockBrownStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BROWN
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BROWN_STAINED_GLASS)
            get() = Companion.field
    }
}