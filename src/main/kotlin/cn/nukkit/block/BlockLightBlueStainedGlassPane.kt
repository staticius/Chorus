package cn.nukkit.block

import cn.nukkit.utils.DyeColor

class BlockLightBlueStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.LIGHT_BLUE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLUE_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}