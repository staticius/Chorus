package org.chorus.block

class BlockRedStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.RED

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_STAINED_GLASS)

    }
}