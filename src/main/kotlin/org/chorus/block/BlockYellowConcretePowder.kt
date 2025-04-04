package org.chorus.block

class BlockYellowConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete() = BlockYellowConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_CONCRETE_POWDER)

    }
}