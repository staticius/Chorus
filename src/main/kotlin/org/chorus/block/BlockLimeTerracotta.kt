package org.chorus.block

class BlockLimeTerracotta @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHardenedClay(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_TERRACOTTA)
            get() = Companion.field
    }
}