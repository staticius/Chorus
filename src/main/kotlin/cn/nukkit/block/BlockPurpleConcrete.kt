package cn.nukkit.block

class BlockPurpleConcrete @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockConcrete(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_CONCRETE)
            get() = Companion.field
    }
}