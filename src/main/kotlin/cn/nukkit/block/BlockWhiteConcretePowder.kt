package cn.nukkit.block

class BlockWhiteConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockWhiteConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_CONCRETE_POWDER)
            get() = Companion.field
    }
}