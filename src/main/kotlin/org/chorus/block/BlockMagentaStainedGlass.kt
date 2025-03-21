package org.chorus.block

import org.chorus.utils.DyeColor

class BlockMagentaStainedGlass @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override fun getDyeColor()  = DyeColor.MAGENTA

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_STAINED_GLASS)

    }
}