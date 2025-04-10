package org.chorus.block

import org.chorus.utils.DyeColor

class BlockMagentaStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor() = DyeColor.MAGENTA

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_STAINED_GLASS_PANE)

    }
}