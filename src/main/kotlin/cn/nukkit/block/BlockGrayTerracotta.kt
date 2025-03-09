package cn.nukkit.block

class BlockGrayTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(GRAY_TERRACOTTA)
            get() = Companion.field
    }
}