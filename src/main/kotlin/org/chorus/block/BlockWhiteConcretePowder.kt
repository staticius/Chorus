package org.chorus.block

class BlockWhiteConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete() = BlockWhiteConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_CONCRETE_POWDER)

    }
}