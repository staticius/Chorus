package org.chorus.block

class BlockMagentaConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override val concrete: BlockConcrete
        get() = BlockMagentaConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_CONCRETE_POWDER)

    }
}