package org.chorus.block

class BlockRedTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_TERRACOTTA)

    }
}