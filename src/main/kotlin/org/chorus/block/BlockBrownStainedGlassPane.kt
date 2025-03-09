package org.chorus.block

import org.chorus.utils.DyeColor

class BlockBrownStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BROWN
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BROWN_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}