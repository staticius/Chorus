package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockYellowStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.YELLOW

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_STAINED_GLASS)
            get() = Companion.field
    }
}