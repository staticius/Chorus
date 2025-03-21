package org.chorus.block

class BlockYellowConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete() = BlockYellowConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_CONCRETE_POWDER)

    }
}