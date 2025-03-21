package org.chorus.block

class BlockMagentaConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete() = BlockMagentaConcrete()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_CONCRETE_POWDER)

    }
}