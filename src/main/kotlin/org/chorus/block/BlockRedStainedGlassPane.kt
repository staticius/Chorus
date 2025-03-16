package org.chorus.block

class BlockRedStainedGlassPane @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockGlassPaneStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.RED

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_STAINED_GLASS_PANE)

    }
}