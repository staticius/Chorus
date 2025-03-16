package org.chorus.block

class BlockPurpleConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockPurpleConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_CONCRETE_POWDER)

    }
}