package cn.nukkit.block

class BlockGrayConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(GRAY_CONCRETE)
            get() = Companion.field
    }
}