package cn.nukkit.block

class BlockWhiteConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_CONCRETE)
            get() = Companion.field
    }
}