package org.chorus.block

import org.chorus.utils.DyeColor

class BlockBlackStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BLACK
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLACK_STAINED_GLASS_PANE)
    }
}