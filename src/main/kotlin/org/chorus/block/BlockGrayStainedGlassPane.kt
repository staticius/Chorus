package org.chorus.block

import org.chorus.utils.DyeColor

class BlockGrayStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {

    override fun getDyeColor() = DyeColor.GRAY

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRAY_STAINED_GLASS_PANE)
    }
}