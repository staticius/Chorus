package cn.nukkit.block

class BlockGreenConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockGreenConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(GREEN_CONCRETE_POWDER)
            get() = Companion.field
    }
}