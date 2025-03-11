package org.chorus.block

class BlockCyanTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CYAN_TERRACOTTA)

    }
}