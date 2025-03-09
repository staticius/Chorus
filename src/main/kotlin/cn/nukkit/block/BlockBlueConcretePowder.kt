package cn.nukkit.block

class BlockBlueConcretePowder @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcretePowder(blockstate) {
    override fun getConcrete(): BlockConcrete {
        return BlockBlueConcrete()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BLUE_CONCRETE_POWDER)
            get() = Companion.field
    }
}