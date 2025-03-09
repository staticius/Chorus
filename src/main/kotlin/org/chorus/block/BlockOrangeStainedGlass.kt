package org.chorus.block

import cn.nukkit.utils.DyeColor

class BlockOrangeStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.ORANGE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_STAINED_GLASS)
            get() = Companion.field
    }
}