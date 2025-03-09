package cn.nukkit.block

class BlockYellowConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockYellowConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_CONCRETE_POWDER)
            get() = Companion.field
    }
}