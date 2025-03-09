package cn.nukkit.block

class BlockMagentaTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_TERRACOTTA)
            get() = Companion.field
    }
}