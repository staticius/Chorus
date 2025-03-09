package cn.nukkit.block

import cn.nukkit.utils.DyeColor

class BlockPinkStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.PINK

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}