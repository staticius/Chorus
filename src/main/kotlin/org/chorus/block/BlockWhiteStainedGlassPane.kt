package org.chorus.block

class BlockWhiteStainedGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.WHITE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}