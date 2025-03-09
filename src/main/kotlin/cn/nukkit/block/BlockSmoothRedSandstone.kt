package cn.nukkit.block

class BlockSmoothRedSandstone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSmoothSandstone(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SMOOTH_RED_SANDSTONE)
            get() = Companion.field
    }
}
