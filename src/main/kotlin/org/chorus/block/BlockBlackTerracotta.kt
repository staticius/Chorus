package org.chorus.block

class BlockBlackTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BLACK_TERRACOTTA)
            get() = Companion.field
    }
}