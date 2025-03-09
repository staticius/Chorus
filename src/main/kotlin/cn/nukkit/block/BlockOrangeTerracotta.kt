package cn.nukkit.block

class BlockOrangeTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_TERRACOTTA)
            get() = Companion.field
    }
}