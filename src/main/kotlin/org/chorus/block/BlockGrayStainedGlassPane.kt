package org.chorus.block

import org.chorus.utils.DyeColor

class BlockGrayStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.GRAY

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRAY_STAINED_GLASS_PANE)

    }
}