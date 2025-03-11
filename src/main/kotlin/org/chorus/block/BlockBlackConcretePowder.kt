package org.chorus.block

class BlockBlackConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete(): BlockConcrete {
        return BlockBlackConcrete()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLACK_CONCRETE_POWDER)

    }
}