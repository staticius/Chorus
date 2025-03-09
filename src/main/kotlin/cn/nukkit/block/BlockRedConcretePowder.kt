package cn.nukkit.block

class BlockRedConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockRedConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_CONCRETE_POWDER)
            get() = Companion.field
    }
}