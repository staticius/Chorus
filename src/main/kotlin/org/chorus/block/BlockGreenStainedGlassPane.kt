package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockGreenStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.GREEN

    companion object {
        val properties: BlockProperties = BlockProperties(GREEN_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}