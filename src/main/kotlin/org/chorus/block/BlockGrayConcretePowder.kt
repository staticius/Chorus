package org.chorus.block

class BlockGrayConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockGrayConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRAY_CONCRETE_POWDER)

    }
}