package cn.nukkit.block

class BlockPurpleCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_CARPET)
            get() = Companion.field
    }
}