package cn.nukkit.block

class BlockReserved6 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RESERVED6)
            get() = Companion.field
    }
}