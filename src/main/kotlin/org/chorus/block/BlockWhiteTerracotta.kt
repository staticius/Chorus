package org.chorus.block

class BlockWhiteTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_TERRACOTTA)
            get() = Companion.field
    }
}