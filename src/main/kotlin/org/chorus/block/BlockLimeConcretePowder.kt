package org.chorus.block

class BlockLimeConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockLimeConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_CONCRETE_POWDER)
            get() = Companion.field
    }
}