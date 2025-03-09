package cn.nukkit.block

class BlockPinkTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_TERRACOTTA)
            get() = Companion.field
    }
}