package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockPurpleStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.PURPLE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}