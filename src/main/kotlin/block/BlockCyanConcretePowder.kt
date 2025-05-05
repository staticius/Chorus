package org.chorus_oss.chorus.block

class BlockCyanConcretePowder @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {

    override fun getConcrete(): BlockConcrete {
        return BlockCyanConcrete()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CYAN_CONCRETE_POWDER)
    }
}