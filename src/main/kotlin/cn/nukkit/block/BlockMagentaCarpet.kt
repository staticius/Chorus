package cn.nukkit.block

class BlockMagentaCarpet @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCarpet(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_CARPET)
            get() = Companion.field
    }
}