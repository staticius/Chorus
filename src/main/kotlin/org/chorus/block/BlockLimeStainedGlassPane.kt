package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockLimeStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.LIME

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}