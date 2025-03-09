package cn.nukkit.block

class BlockWhiteCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_CARPET)
            get() = Companion.field
    }
}