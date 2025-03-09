package cn.nukkit.block

class BlockCyanStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassStained(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.CYAN
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CYAN_STAINED_GLASS)
            get() = Companion.field
    }
}