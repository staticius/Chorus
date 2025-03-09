package org.chorus.block

class BlockCyanTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CYAN_TERRACOTTA)
            get() = Companion.field
    }
}