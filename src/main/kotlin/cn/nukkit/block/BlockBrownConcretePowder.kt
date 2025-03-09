package cn.nukkit.block

class BlockBrownConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete(): BlockConcrete {
        return BlockBrownConcrete()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BROWN_CONCRETE_POWDER)
            get() = Companion.field
    }
}