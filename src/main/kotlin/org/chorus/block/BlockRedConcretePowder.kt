package org.chorus.block

class BlockRedConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete() = BlockRedConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_CONCRETE_POWDER)

    }
}