package org.chorus.block

import org.chorus.utils.DyeColor

class BlockPurpleStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor()  = DyeColor.PURPLE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_STAINED_GLASS_PANE)

    }
}