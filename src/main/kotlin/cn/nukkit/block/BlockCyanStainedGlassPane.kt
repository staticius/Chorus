package cn.nukkit.block

class BlockCyanStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override fun getDyeColor(): DyeColor {
        return DyeColor.CYAN
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CYAN_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}