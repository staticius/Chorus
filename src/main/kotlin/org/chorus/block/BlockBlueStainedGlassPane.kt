package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockBlueStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BLUE
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BLUE_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}