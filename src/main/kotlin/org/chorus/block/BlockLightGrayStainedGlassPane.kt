package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockLightGrayStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.LIGHT_GRAY

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_GRAY_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}