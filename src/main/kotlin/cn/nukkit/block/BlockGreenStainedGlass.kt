package cn.nukkit.block

import cn.nukkit.utils.DyeColor

class BlockGreenStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.GREEN

    companion object {
        val properties: BlockProperties = BlockProperties(GREEN_STAINED_GLASS)
            get() = Companion.field
    }
}