package cn.nukkit.block

class BlockBlackConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BLACK_CONCRETE)
            get() = Companion.field
    }
}