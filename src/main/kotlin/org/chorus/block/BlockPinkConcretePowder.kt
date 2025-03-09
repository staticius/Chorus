package org.chorus.block

class BlockPinkConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockPinkConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_CONCRETE_POWDER)
            get() = Companion.field
    }
}