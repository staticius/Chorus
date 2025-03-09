package cn.nukkit.block

import cn.nukkit.utils.DyeColor

class BlockPurpleStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.PURPLE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_STAINED_GLASS)
            get() = Companion.field
    }
}