package cn.nukkit.block

class BlockBrownConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BROWN_CONCRETE)
            get() = Companion.field
    }
}