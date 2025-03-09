package cn.nukkit.block

import cn.nukkit.utils.DyeColor

class BlockBlackStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.BLACK
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BLACK_STAINED_GLASS)
            get() = Companion.field
    }
}