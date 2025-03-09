package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockPinkStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.PINK

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_STAINED_GLASS)
            get() = Companion.field
    }
}