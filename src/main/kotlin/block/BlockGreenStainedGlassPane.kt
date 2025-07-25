package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.utils.DyeColor

class BlockGreenStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = properties.defaultState) :
    BlockGlassPaneStained(blockstate) {

    override fun getDyeColor() = DyeColor.GREEN

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GREEN_STAINED_GLASS_PANE)
    }
}