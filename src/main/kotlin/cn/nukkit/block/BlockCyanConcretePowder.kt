package cn.nukkit.block

class BlockCyanConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockCyanConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(CYAN_CONCRETE_POWDER)
            get() = Companion.field
    }
}