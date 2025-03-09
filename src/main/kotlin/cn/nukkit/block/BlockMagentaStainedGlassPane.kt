package cn.nukkit.block

import cn.nukkit.utils.DyeColor

class BlockMagentaStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.MAGENTA

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}