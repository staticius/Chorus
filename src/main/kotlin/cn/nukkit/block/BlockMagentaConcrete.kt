package cn.nukkit.block

class BlockMagentaConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_CONCRETE)
            get() = Companion.field
    }
}