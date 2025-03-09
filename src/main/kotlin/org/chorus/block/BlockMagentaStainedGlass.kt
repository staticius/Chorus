package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockMagentaStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.MAGENTA

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_STAINED_GLASS)
            get() = Companion.field
    }
}