package org.chorus.block

import org.chorus.utils.DyeColor

class BlockGreenStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {

    override fun getDyeColor() = DyeColor.GREEN

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GREEN_STAINED_GLASS_PANE)
    }
}