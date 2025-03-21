package org.chorus.block

class BlockLimeConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete() = BlockLimeConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_CONCRETE_POWDER)

    }
}