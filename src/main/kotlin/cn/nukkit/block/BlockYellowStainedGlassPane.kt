package cn.nukkit.block

import cn.nukkit.utils.DyeColor

class BlockYellowStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.YELLOW

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}