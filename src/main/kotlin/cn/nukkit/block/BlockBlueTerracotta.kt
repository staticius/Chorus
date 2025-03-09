package cn.nukkit.block

class BlockBlueTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BLUE_TERRACOTTA)
            get() = Companion.field
    }
}