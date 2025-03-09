package cn.nukkit.block

class BlockGrayConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockGrayConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(GRAY_CONCRETE_POWDER)
            get() = Companion.field
    }
}