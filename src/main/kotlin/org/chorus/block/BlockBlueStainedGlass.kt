package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockBlueStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BLUE
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BLUE_STAINED_GLASS)
            get() = Companion.field
    }
}