package cn.nukkit.block

class BlockGreenConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(GREEN_CONCRETE)
            get() = Companion.field
    }
}