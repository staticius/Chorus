package org.chorus.block

class BlockOrangeConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockOrangeConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_CONCRETE_POWDER)
            get() = Companion.field
    }
}