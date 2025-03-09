package cn.nukkit.block

class BlockRedCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_CARPET)
            get() = Companion.field
    }
}