package cn.nukkit.block

class BlockLightGrayTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_GRAY_TERRACOTTA)
            get() = Companion.field
    }
}