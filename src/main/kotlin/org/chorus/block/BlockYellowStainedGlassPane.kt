package org.chorus.block

import org.chorus.utils.DyeColor

class BlockYellowStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor()  = DyeColor.YELLOW

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_STAINED_GLASS_PANE)

    }
}