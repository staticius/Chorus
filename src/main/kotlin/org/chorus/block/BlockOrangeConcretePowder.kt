package org.chorus.block

class BlockOrangeConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete() = BlockOrangeConcrete()

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_CONCRETE_POWDER)
    }
}