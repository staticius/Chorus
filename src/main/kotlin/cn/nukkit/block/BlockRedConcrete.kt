package cn.nukkit.block

class BlockRedConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_CONCRETE)
            get() = Companion.field
    }
}