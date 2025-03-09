package cn.nukkit.block

class BlockPurpleTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_TERRACOTTA)
            get() = Companion.field
    }
}