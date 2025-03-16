package org.chorus.block

class BlockGreenConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockGreenConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GREEN_CONCRETE_POWDER)

    }
}