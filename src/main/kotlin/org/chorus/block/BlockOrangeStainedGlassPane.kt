package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockOrangeStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.ORANGE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}