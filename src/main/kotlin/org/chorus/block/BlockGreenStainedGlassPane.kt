package org.chorus.block

import org.chorus.utils.DyeColor

class BlockGreenStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.GREEN

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GREEN_STAINED_GLASS_PANE)

    }
}