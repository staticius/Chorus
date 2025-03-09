package cn.nukkit.block

class BlockCyanCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CYAN_CARPET)
            get() = Companion.field
    }
}