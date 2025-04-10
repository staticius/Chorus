package org.chorus.block

import org.chorus.utils.DyeColor

class BlockLimeStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor() = DyeColor.LIME

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_STAINED_GLASS_PANE)

    }
}