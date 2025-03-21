package org.chorus.block

import org.chorus.utils.DyeColor

class BlockLightGrayStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor()  = DyeColor.LIGHT_GRAY

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_GRAY_STAINED_GLASS_PANE)

    }
}