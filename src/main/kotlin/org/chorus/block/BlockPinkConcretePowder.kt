package org.chorus.block

class BlockPinkConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete() = BlockPinkConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_CONCRETE_POWDER)

    }
}