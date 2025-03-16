package org.chorus.block

class BlockCyanConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockCyanConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CYAN_CONCRETE_POWDER)

    }
}