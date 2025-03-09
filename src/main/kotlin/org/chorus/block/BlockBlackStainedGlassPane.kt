package org.chorus.block

import org.chorus.utils.DyeColor

class BlockBlackStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BLACK
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BLACK_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}