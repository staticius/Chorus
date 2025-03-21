package org.chorus.block

import org.chorus.utils.DyeColor

class BlockOrangeStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor()  = DyeColor.ORANGE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_STAINED_GLASS_PANE)

    }
}