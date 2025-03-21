package org.chorus.block

class BlockGrayConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {

    override fun getConcrete() = BlockGrayConcrete()

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRAY_CONCRETE_POWDER)
    }
}