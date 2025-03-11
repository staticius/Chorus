package org.chorus.block

class BlockWhiteConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockWhiteConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_CONCRETE_POWDER)

    }
}