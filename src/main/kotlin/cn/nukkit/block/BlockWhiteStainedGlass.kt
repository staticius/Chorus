package cn.nukkit.block

class BlockWhiteStainedGlass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockGlassStained(blockstate) {
    override val dyeColor: DyeColor
        get() = DyeColor.WHITE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_STAINED_GLASS)
            get() = Companion.field
    }
}