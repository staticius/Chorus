package cn.nukkit.block

class BlockYellowConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_CONCRETE)
            get() = Companion.field
    }
}