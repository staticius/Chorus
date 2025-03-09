package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockLightBlueStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.LIGHT_BLUE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLUE_STAINED_GLASS)
            get() = Companion.field
    }
}