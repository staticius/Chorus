package org.chorus.block

class BlockLightBlueConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete() = BlockLightBlueConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLUE_CONCRETE_POWDER)

    }
}